package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.DyedBlockList;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeEncasedShaftBlock extends EncasedShaftBlock implements CarbonComposite<CarbonCompositeBlock> {

    private final DyeColor color;

    public CarbonCompositeEncasedShaftBlock(Properties properties, DyeColor color) {
        super(properties, ModBlocks.CARBON_COMPOSITE_BLOCKS.get(color)::get);
        this.color = color;
        CARBON_COMPOSITES.add(this);
    }

    @Override
    public DyeColor color() {
        return this.color;
    }

    @Override
    public DyedBlockList<CarbonCompositeBlock> dyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_BLOCKS;
    }

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        super.handleEncasing(state, level, pos, heldItem, player, hand, ray);

        if (!player.isCreative())
            player.getItemInHand(hand).shrink(1);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity be) {
        return super.getRequiredItems(state, be).union(this.getItemRequirement());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return CarbonComposite.useItemOn(itemStack, blockState, level, blockPos);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        super.onSneakWrenched(state, context);

        if (context.getLevel() instanceof ServerLevel) {
            Player player = context.getPlayer();

            if (player != null && !player.isCreative())
                player.getInventory().placeItemBackInInventory(this.getOfColor().asStack());
        }

        return InteractionResult.SUCCESS;
    }

}