package dev.spiritstudios.aerobig.block;

import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import dev.spiritstudios.aerobig.registry.AerospaceBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeEncasedShaftBlock extends EncasedShaftBlock implements SpecialBlockItemRequirement {

    protected final DyeColor color;

    public CarbonCompositeEncasedShaftBlock(Properties properties, DyeColor color) {
        super(properties, AerospaceBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.get(color)::get);
        this.color = color;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return CarbonCompositeBlock.useItemOn(itemStack, blockState, level, blockPos);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        super.onSneakWrenched(state, context);

        Level level = context.getLevel();

        if (level instanceof ServerLevel) {
            Player player = context.getPlayer();

            if (player != null && !player.isCreative())
                player.getInventory().placeItemBackInInventory(AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(this.color).asStack());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return AerospaceBlockEntityTypes.CARBON_COMPOSITE_ENCASED_SHAFT.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AerospaceBlockEntityTypes.CARBON_COMPOSITE_ENCASED_SHAFT.create(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return this.getCasing().asItem().getDefaultInstance();
    }

    @Override
    public Block getCasing() {
        return AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(this.color).get();
    }

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        super.handleEncasing(state, level, pos, heldItem, player, hand, ray);

        if (!player.isCreative())
            player.getItemInHand(hand).shrink(1);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity be) {
        ItemStack stack = AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(this.color).asStack();
        return super.getRequiredItems(state, be).union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stack));
    }

}