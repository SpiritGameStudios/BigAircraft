package dev.spiritstudios.aerobig.block;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeWingShaftBlock extends HorizontalAxisKineticBlock implements IBE<KineticBlockEntity>, SpecialBlockItemRequirement, EncasedBlock, CarbonComposite<CarbonCompositeWingBlock>, ProperWaterloggedBlock {

    private final DyeColor color;

    public CarbonCompositeWingShaftBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));

        CARBON_COMPOSITES.add(this);
    }

    @Override
    public DyeColor color() {
        return this.color;
    }

    @Override
    public DyedBlockList<CarbonCompositeWingBlock> dyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_WINGS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CarbonCompositeWingBlock.SHAPE;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return this.fluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.withWater(super.getStateForPlacement(context), context);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        this.updateWater(level, state, pos);
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity blockEntity) {
        return ItemRequirement.of(AllBlocks.SHAFT.getDefaultState(), blockEntity).union(this.getItemRequirement());
    }


    @Override
    public Block getCasing() {
        return this.getOfColor().get();
    }

    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        Direction.Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
        assert axis.isHorizontal();

        KineticBlockEntity.switchToBlockState(level, pos, this.defaultBlockState().setValue(HORIZONTAL_AXIS, axis));

        if (!player.isCreative())
            player.getItemInHand(hand).shrink(1);
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.ENCASED_SHAFT.get();
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (target instanceof BlockHitResult blockHitResult)
            return blockHitResult.getDirection().getAxis() == this.getRotationAxis(state) ? AllBlocks.SHAFT.asStack() : this.getOfColor().asStack();

        return super.getCloneItemStack(state, target, level, pos, player);
    }

}
