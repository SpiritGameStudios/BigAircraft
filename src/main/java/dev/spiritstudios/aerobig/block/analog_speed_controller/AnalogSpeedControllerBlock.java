package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import dev.spiritstudios.aerobig.registry.ModBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnalogSpeedControllerBlock extends HorizontalAxisKineticBlock implements IBE<AnalogSpeedControllerBlockEntity> {

    private static final int PLACEMENT_HELPER_ID = PlacementHelpers.register(new PlacementHelper());
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape SHAPE = box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    public AnalogSpeedControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (AnalogSpeedControllerBlockEntity.isCogwheelPresent(level, clickedPos)) {
            BlockState above = level.getBlockState(clickedPos.above());
            blockState = this.defaultBlockState().setValue(HORIZONTAL_AXIS, above.getValue(CogWheelBlock.AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        }

        if (blockState != null)
            blockState = blockState.setValue(POWERED, level.hasNeighborSignal(clickedPos));

        return blockState;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbourBlock, BlockPos neighbourPos, boolean movedByPiston) {
        if (level.isClientSide)
            return;

        this.withBlockEntityDo(level, pos, blockEntity -> {
            if (neighbourPos.equals(pos.above()))
                blockEntity.updateBracket();

            blockEntity.neighbourChanged();
        });

        if (state.getValue(POWERED) != level.hasNeighborSignal(pos))
            level.setBlock(pos, state.cycle(POWERED), UPDATE_CLIENTS | UPDATE_KNOWN_SHAPE);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);

        if (oldState.getBlock() != state.getBlock())
            this.withBlockEntityDo(worldIn, pos, AnalogSpeedControllerBlockEntity::neighbourChanged);
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return super.areStatesKineticallyEquivalent(oldState, newState) && oldState.getValue(POWERED) == newState.getValue(POWERED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IPlacementHelper helper = PlacementHelpers.get(PLACEMENT_HELPER_ID);

        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult).placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public Class<AnalogSpeedControllerBlockEntity> getBlockEntityClass() {
        return AnalogSpeedControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AnalogSpeedControllerBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.ANALOG_SPEED_CONTROLLER.get();
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return itemStack -> ICogWheel.isLargeCogItem(itemStack) && ICogWheel.isDedicatedCogItem(itemStack);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ModBlocks.ANALOG_SPEED_CONTROLLER::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level level, BlockState state, BlockPos pos, BlockHitResult ray) {
            BlockPos newPos = pos.above();

            if (!level.getBlockState(newPos).canBeReplaced())
                return PlacementOffset.fail();

            Direction.Axis newAxis = state.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

            if (!CogWheelBlock.isValidCogwheelPosition(true, level, newPos, newAxis))
                return PlacementOffset.fail();

            return PlacementOffset.success(newPos, s -> s.setValue(CogWheelBlock.AXIS, newAxis));
        }

    }

}
