package dev.spiritstudios.aerobig.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {

    @WrapOperation(method = "isConnected", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/RotationPropagator;isLargeCogToSpeedController(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean connectAnalogController(BlockState from, BlockState _to, BlockPos diff, Operation<Boolean> original, KineticBlockEntity fromBe, KineticBlockEntity toBe) {
        return original.call(from, _to, diff) || bigAircraft$isLargeCogToAnalogSpeedController(fromBe, toBe);
    }

    @WrapMethod(method = "getConveyedSpeed")
    private static float addAnalogController(KineticBlockEntity from, KineticBlockEntity _to, Operation<Float> original) {
        if (bigAircraft$isLargeCogToAnalogSpeedController(from, _to))
            return AnalogSpeedControllerBlockEntity.getConveyedSpeed(from, _to, true);

        if (bigAircraft$isLargeCogToAnalogSpeedController(_to, from))
            return AnalogSpeedControllerBlockEntity.getConveyedSpeed(_to, from, false);

        return original.call(from, _to);
    }

    @Unique
    private static boolean bigAircraft$isLargeCogToAnalogSpeedController(KineticBlockEntity from, KineticBlockEntity to) {
        BlockState stateFrom = from.getBlockState();
        BlockState stateTo = to.getBlockState();

        if (!ICogWheel.isLargeCog(stateFrom) || !AerospaceBlocks.ANALOG_SPEED_CONTROLLER.has(stateTo))
            return false;

        BlockPos diff = from.getBlockPos().subtract(to.getBlockPos());

        if (!diff.equals(BlockPos.ZERO.below()))
            return false;

        Direction.Axis axis = stateFrom.getValue(CogWheelBlock.AXIS);

        return !axis.isVertical() && stateTo.getValue(AnalogSpeedControllerBlock.HORIZONTAL_AXIS) != axis;
    }

}
