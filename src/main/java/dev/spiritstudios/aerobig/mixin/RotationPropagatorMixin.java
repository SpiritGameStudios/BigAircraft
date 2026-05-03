package dev.spiritstudios.aerobig.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {
    @WrapMethod(method = "getConveyedSpeed")
    private static float addAnalogController(KineticBlockEntity from, KineticBlockEntity to, Operation<Float> original) {
        final BlockState stateFrom = from.getBlockState();
        final BlockState stateTo = to.getBlockState();

        if (bigAircraft$isLargeCogToAnalogSpeedController(stateFrom, stateTo, to.getBlockPos()
                .subtract(from.getBlockPos())))
            return AnalogSpeedControllerBlockEntity.getConveyedSpeed(from, to, true);
        if (bigAircraft$isLargeCogToAnalogSpeedController(stateTo, stateFrom, from.getBlockPos()
                .subtract(to.getBlockPos())))
            return AnalogSpeedControllerBlockEntity.getConveyedSpeed(to, from, false);

        return original.call(from, to);
    }

    @Unique
    private static boolean bigAircraft$isLargeCogToAnalogSpeedController(BlockState from, BlockState to, BlockPos diff) {
        if (!ICogWheel.isLargeCog(from) || !AerospaceBlocks.ANALOG_SPEED_CONTROLLER.has(to))
            return false;
        if (!diff.equals(BlockPos.ZERO.below()))
            return false;
        Direction.Axis axis = from.getValue(CogWheelBlock.AXIS);
        if (axis.isVertical())
            return false;
        if (to.getValue(AnalogSpeedControllerBlock.HORIZONTAL_AXIS) == axis)
            return false;
        return true;

    }
}
