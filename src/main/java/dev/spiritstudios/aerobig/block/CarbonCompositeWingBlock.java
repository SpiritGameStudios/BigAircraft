package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeWingBlock extends Block implements ProperWaterloggedBlock, IWrenchable, ICarbonCompositeWing {

    static final VoxelShape SHAPE = box(0.0, 4.0, 0.0, 16.0, 12.0, 16.0);

    private final DyeColor color;

    public CarbonCompositeWingBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return CarbonComposite.useItemOn(itemStack, blockState, level, blockPos);
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        builder.add(WATERLOGGED);
    }

}
