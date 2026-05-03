package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import dev.spiritstudios.aerobig.registry.AerospaceBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class AnalogSpeedControllerBlock extends HorizontalAxisKineticBlock implements IBE<AnalogSpeedControllerBlockEntity> {
    private static final int placementHelperId = PlacementHelpers.register(new AnalogSpeedControllerBlock.PlacementHelper());

    public AnalogSpeedControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState above = context.getLevel()
                .getBlockState(context.getClickedPos()
                        .above());
        if (ICogWheel.isLargeCog(above) && above.getValue(CogWheelBlock.AXIS)
                .isHorizontal())
            return defaultBlockState().setValue(HORIZONTAL_AXIS, above.getValue(CogWheelBlock.AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        return super.getStateForPlacement(context);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighbourBlock, BlockPos neighbourPos,
                                boolean movedByPiston) {
        if (neighbourPos.equals(pos.above()))
            withBlockEntityDo(world, pos, AnalogSpeedControllerBlockEntity::updateBracket);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult).placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.SPEED_CONTROLLER;
    }

    @Override
    public Class<AnalogSpeedControllerBlockEntity> getBlockEntityClass() {
        return AnalogSpeedControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AnalogSpeedControllerBlockEntity> getBlockEntityType() {
        return AerospaceBlockEntityTypes.ANALOG_SPEED_CONTROLLER.get();
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return ((Predicate<ItemStack>) ICogWheel::isLargeCogItem).and(ICogWheel::isDedicatedCogItem);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return AerospaceBlocks.ANALOG_SPEED_CONTROLLER::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            BlockPos newPos = pos.above();
            if (!world.getBlockState(newPos)
                    .canBeReplaced())
                return PlacementOffset.fail();

            Direction.Axis newAxis = state.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

            if (!CogWheelBlock.isValidCogwheelPosition(true, world, newPos, newAxis))
                return PlacementOffset.fail();

            return PlacementOffset.success(newPos, s -> s.setValue(CogWheelBlock.AXIS, newAxis));
        }
    }
}
