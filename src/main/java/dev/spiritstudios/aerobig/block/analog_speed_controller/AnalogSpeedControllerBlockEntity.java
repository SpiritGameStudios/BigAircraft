package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class AnalogSpeedControllerBlockEntity extends KineticBlockEntity {

    private static final String SIGNAL_KEY = "Signal";

    private int signal = 0;
    public boolean hasBracket = false;
    private boolean signalChanged = false;

    public AnalogSpeedControllerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.setLazyTickRate(40);
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
            this.signalChanged = true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        this.updateBracket();
        this.neighbourChanged();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level == null || this.level.isClientSide)
            return;

        if (this.signalChanged) {
            this.signalChanged = false;
            this.analogSignalChanged(this.getSignalAt());
        }
    }

    protected void analogSignalChanged(int newSignal) {
        this.detachKinetics();
        this.removeSource();
        this.signal = newSignal;
        this.attachKinetics();
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
            0.0F, AllConfigs.server().kinetics.maxRotationSpeed.get()
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
}
