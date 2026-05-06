package dev.spiritstudios.aerobig.mixin.encasing;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EncasableBlock.class)
public interface EncasableBlockMixin {

    @Inject(method = "tryEncase", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", opcode = Opcodes.GETFIELD), cancellable = true)
    private void onlyEncaseShaftWithWingWhenHorizontal(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<ItemInteractionResult> cir, @Local(name = "block") Block block) {
        if (!ModBlocks.CARBON_COMPOSITE_WING_SHAFTS.contains(block))
            return;

        if (state.getBlock() instanceof AbstractShaftBlock && !state.getValue(RotatedPillarKineticBlock.AXIS).isHorizontal())
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
    }

}
