package dev.spiritstudios.aerobig.block.carbon_composite;

import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.util.OrderedDyedEntryList;
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
import net.minecraft.world.level.block.Block;
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
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
    }

    @Override
    public OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeBlock>> getDyedVariants() {
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return CarbonComposite.useItemOn(stack, state, level, pos, player, hand, hitResult, null);
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

    @Override
    public DyeableGroup getDyeableGroup() {
        return DyeableGroup.FUSELAGE;
    }

}