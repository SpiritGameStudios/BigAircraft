package dev.spiritstudios.aerobig.mixin.analog_speed_controller;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CogWheelBlock.class)
public class CogWheelBlockMixin {

    @WrapOperation(method = "getAxisForPlacement", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean alignWithAnalogSpeedController(BlockEntry<SpeedControllerBlock> instance, BlockState state, Operation<Boolean> original) {
        return original.call(instance, state) || original.call(ModBlocks.ANALOG_SPEED_CONTROLLER, state);
    }

}
