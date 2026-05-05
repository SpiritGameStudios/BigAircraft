package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.spiritstudios.aerobig.registry.ModAdvancements;
import dev.spiritstudios.aerobig.registry.ModBlockEntityTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.Objects;

public class AnalogSpeedControllerBlockEntity extends KineticBlockEntity {

    private static final int DEFAULT_SPEED = 16;
    private static final String SIGNAL_KEY = "Signal";

    private int signal = 0;
    public boolean hasBracket = false;

    public ScrollValueBehaviour targetSpeed;
    public AbstractComputerBehaviour computerBehaviour;

    public AnalogSpeedControllerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public static void registerComputerBehaviour(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            PeripheralCapability.get(),
            ModBlockEntityTypes.ANALOG_SPEED_CONTROLLER.get(),
            (be, context) -> be.computerBehaviour.getPeripheralCapability()
        );
    }

    @Override
    protected void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putInt(SIGNAL_KEY, this.signal);
    }

    @Override
    protected void read(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        this.signal = compound.getInt(SIGNAL_KEY);
    }

    public void neighbourChanged() {
        if (this.level != null && this.getSignalAt() != this.signal)
            this.analogSignalChanged(this.getSignalAt());
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        this.updateBracket();
        this.neighbourChanged();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.computerBehaviour.removePeripheral();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = AllConfigs.server().kinetics.maxRotationSpeed.get();

        this.targetSpeed = new KineticScrollValueBehaviour(CreateLang.translateDirect("kinetics.speed_controller.rotation_speed"), this, new ControllerValueBoxTransform());
        this.targetSpeed.between(-max, max);
        this.targetSpeed.value = DEFAULT_SPEED;
        this.targetSpeed.withCallback(i -> this.analogSignalChanged(this.getSignalAt()));

        behaviours.add(this.targetSpeed);
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
    }

    protected void analogSignalChanged(int newSignal) {
        this.detachKinetics();
        this.removeSource();
        this.signal = newSignal;
        this.attachKinetics();

        if (this.level != null && isCogwheelPresent(this.level, this.worldPosition))
            ModAdvancements.ANALOG_SPEED_CONTROLLER.awardToNearby(worldPosition, level);
    }

    public void updateBracket() {
        if (this.level != null && this.level.isClientSide)
            this.hasBracket = isCogwheelPresent(this.level, this.worldPosition);
    }

    private int getSignalAt() {
        return Objects.requireNonNull(this.level).getBestNeighborSignal(this.worldPosition);
    }

    public static boolean isCogwheelPresent(Level level, BlockPos blockPos) {
        BlockState stateAbove = level.getBlockState(blockPos.above());
        return ICogWheel.isDedicatedCogWheel(stateAbove.getBlock()) && ICogWheel.isLargeCog(stateAbove) && stateAbove.getValue(CogWheelBlock.AXIS).isHorizontal();
    }

    public static float getConveyedSpeed(KineticBlockEntity cogWheel, KineticBlockEntity speedControllerIn, boolean targetingController) {
        if (!(speedControllerIn instanceof AnalogSpeedControllerBlockEntity analogSpeedController))
            return 0;

        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();
        float desiredOutputSpeed = analogSpeedController.getDesiredOutputSpeed(cogWheel, targetingController);

        float compareSpeed = targetingController ? speed : wheelSpeed;

        if (desiredOutputSpeed >= 0 && compareSpeed >= 0)
            return Math.max(desiredOutputSpeed, compareSpeed);

        if (desiredOutputSpeed < 0 && compareSpeed < 0)
            return Math.min(desiredOutputSpeed, compareSpeed);

        return desiredOutputSpeed;
    }

    public float getDesiredOutputSpeed(KineticBlockEntity cogWheel, boolean targetingController) {
        float targetSpeed = Mth.map(
            this.signal,
            0.0F, 15.0F,
            0.0F, this.targetSpeed.value
        );

        float speed = this.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();

        if (targetSpeed == 0 || targetingController && wheelSpeed == 0)
            return 0;

        if (this.source == null)
            return targetingController ? targetSpeed : 0;

        boolean wheelPowersController = this.source.equals(cogWheel.getBlockPos());

        if (wheelPowersController)
            return targetingController ? targetSpeed : wheelSpeed;

        return targetingController ? speed : targetSpeed;
    }

    private static class ControllerValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0, 10.0, 15.5);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            if (direction.getAxis().isVertical())
                return false;

            return state.getValue(AnalogSpeedControllerBlock.HORIZONTAL_AXIS) != direction.getAxis();
        }

    }
}
