package dev.spiritstudios.aerobig.block.carbon_composite;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import dev.simulated_team.simulated.util.placement_helpers.SymmetricSailPlacementHelper;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.registry.ModTags;
import dev.spiritstudios.aerobig.util.OrderedDyedEntryList;
import net.createmod.catnip.math.VoxelShaper;
import net.createmod.catnip.placement.PlacementHelpers;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeStabilizerBlock extends RotatedPillarBlock implements ProperWaterloggedBlock, IWrenchable, BlockSubLevelLiftProvider, CarbonComposite<CarbonCompositeStabilizerBlock> {

    private static final int HELPER_ID = PlacementHelpers.register(new SymmetricSailPlacementHelper(stack -> stack.is(ModTags.Items.CARBON_COMPOSITE_STABILIZERS), state -> state.getBlock() instanceof CarbonCompositeStabilizerBlock));
    public static final VoxelShaper SHAPE = new AllShapes.Builder(box(0.0, 4.0, 0.0, 16.0, 12.0, 16.0)).forAxis();

    private final DyeColor color;

    public CarbonCompositeStabilizerBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return CarbonComposite.useItemOn(stack, state, level, pos, player, hand, hitResult, PlacementHelpers.get(HELPER_ID));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(AXIS));
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
    public float sable$getLiftScalar() {
        return 0;
    }

    @Override
    public float sable$getParallelDragScalar() {
        return 1.75F;
    }

    @Override
    public Direction sable$getNormal(BlockState blockState) {
        return Direction.get(Direction.AxisDirection.POSITIVE, blockState.getValue(AXIS));
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
    }

    @Override
    public OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeStabilizerBlock>> getDyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_STABILIZERS;
    }

    @Override
    public DyeableGroup getDyeableGroup() {
        return DyeableGroup.CONTROL_SURFACE;
    }

}
