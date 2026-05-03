package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.spiritstudios.aerobig.registry.AerospaceAdvancements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnalogSpeedControllerBlockEntity extends KineticBlockEntity {
    public boolean hasBracket = false;
    private int signal = 0;

    public AnalogSpeedControllerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    private void updateTargetRotation() {
        if (hasNetwork())
            getOrCreateNetwork().remove(this);
        RotationPropagator.handleRemoved(level, worldPosition, this);
        removeSource();
        attachKinetics();

        if (isCogwheelPresent())
            AerospaceAdvancements.ANALOG_SPEED_CONTROLLER.awardToNearby(worldPosition, level);
    }

    @Override
    protected void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.putInt("Signal", this.signal);
    }

    @Override
    protected void read(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        this.signal = compound.getInt("Signal");
    }

    @Override
    public void tick() {
        final int bestNeighborSignal = this.getLevel().getBestNeighborSignal(this.getBlockPos());

        if (bestNeighborSignal != this.signal) {
            this.signal = bestNeighborSignal;
            updateTargetRotation();
        }

        super.tick();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        updateBracket();
    }

    public void updateBracket() {
        if (level != null && level.isClientSide)
            hasBracket = isCogwheelPresent();
    }

    private boolean isCogwheelPresent() {
        BlockState stateAbove = level.getBlockState(worldPosition.above());
        return ICogWheel.isDedicatedCogWheel(stateAbove.getBlock()) && ICogWheel.isLargeCog(stateAbove)
                && stateAbove.getValue(CogWheelBlock.AXIS)
                .isHorizontal();
    }

    public static float getConveyedSpeed(KineticBlockEntity cogWheel, KineticBlockEntity speedControllerIn,
                                         boolean targetingController) {
        if (!(speedControllerIn instanceof AnalogSpeedControllerBlockEntity))
            return 0;

        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();
        float desiredOutputSpeed = getDesiredOutputSpeed(cogWheel, speedControllerIn, targetingController);

        float compareSpeed = targetingController ? speed : wheelSpeed;
        if (desiredOutputSpeed >= 0 && compareSpeed >= 0)
            return Math.max(desiredOutputSpeed, compareSpeed);
        if (desiredOutputSpeed < 0 && compareSpeed < 0)
            return Math.min(desiredOutputSpeed, compareSpeed);

        return desiredOutputSpeed;
    }

    public static float getDesiredOutputSpeed(KineticBlockEntity cogWheel, KineticBlockEntity speedControllerIn,
                                              boolean targetingController) {
        AnalogSpeedControllerBlockEntity speedController = (AnalogSpeedControllerBlockEntity) speedControllerIn;
        float targetSpeed = Mth.map(
                speedController.signal,
                0F, 15F,
                0F, AllConfigs.server().kinetics.maxRotationSpeed.get()
        );

        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();

        if (targetSpeed == 0)
            return 0;
        if (targetingController && wheelSpeed == 0)
            return 0;
        if (!speedController.hasSource()) {
            if (targetingController)
                return targetSpeed;
            return 0;
        }

        boolean wheelPowersController = speedController.source.equals(cogWheel.getBlockPos());

        if (wheelPowersController) {
            if (targetingController)
                return targetSpeed;
            return wheelSpeed;
        }

        if (targetingController)
            return speed;
        return targetSpeed;
    }
}
