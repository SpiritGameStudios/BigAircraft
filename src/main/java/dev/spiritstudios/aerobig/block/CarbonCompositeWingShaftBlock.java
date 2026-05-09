package dev.spiritstudios.aerobig.block;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
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
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeWingShaftBlock extends HorizontalAxisKineticBlock implements IBE<KineticBlockEntity>, SpecialBlockItemRequirement, EncasedBlock, ProperWaterloggedBlock, ICarbonCompositeWing {

    public static final BooleanProperty CONNECT_POSITIVE = BooleanProperty.create("connect_positive");
    public static final BooleanProperty CONNECT_NEGATIVE = BooleanProperty.create("connect_negative");

    private final DyeColor color;

    public CarbonCompositeWingShaftBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.getStateDefinition().any()
            .setValue(WATERLOGGED, false)
            .setValue(CONNECT_POSITIVE, false)
            .setValue(CONNECT_NEGATIVE, false)
        );
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
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
        BlockState stateForPlacement = this.defaultBlockState().setValue(HORIZONTAL_AXIS, Objects.requireNonNullElse(
            getPreferredHorizontalAxis(context),
            context.getHorizontalDirection().getAxis()
        ));

        Direction.Axis axis = stateForPlacement.getValue(HORIZONTAL_AXIS);

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (level.getBlockState(relativeAxisDirectionPos(clickedPos, axis, false)).getBlock() instanceof ICarbonCompositeWing)
            stateForPlacement = stateForPlacement.setValue(CONNECT_POSITIVE, true);

        if (level.getBlockState(relativeAxisDirectionPos(clickedPos, axis, true)).getBlock() instanceof ICarbonCompositeWing)
            stateForPlacement = stateForPlacement.setValue(CONNECT_NEGATIVE, true);

        return this.withWater(stateForPlacement, context);
    }

    private static BlockPos relativeAxisDirectionPos(BlockPos pos, Direction.Axis axis, boolean positive) {
        return pos.relative(Direction.fromAxisAndDirection(axis, positive ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        this.updateWater(level, state, pos);
        Direction.Axis axis = state.getValue(HORIZONTAL_AXIS);

        if (direction.getAxis() != axis)
            return state;

        return state.setValue(ofAxisDirection(direction.getAxisDirection().opposite()), neighborState.getBlock() instanceof ICarbonCompositeWing);
    }

    private static BooleanProperty ofAxisDirection(Direction.AxisDirection axisDirection) {
        return axisDirection == Direction.AxisDirection.POSITIVE ? CONNECT_POSITIVE : CONNECT_NEGATIVE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED, CONNECT_POSITIVE, CONNECT_NEGATIVE));
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

        BlockState blockState = this.defaultBlockState().setValue(HORIZONTAL_AXIS, axis);

        if (level.getBlockState(relativeAxisDirectionPos(pos, axis, false)).getBlock() instanceof ICarbonCompositeWing)
            blockState = blockState.setValue(CONNECT_POSITIVE, true);

        if (level.getBlockState(relativeAxisDirectionPos(pos, axis, true)).getBlock() instanceof ICarbonCompositeWing)
            blockState = blockState.setValue(CONNECT_NEGATIVE, true);

        KineticBlockEntity.switchToBlockState(level, pos, blockState);

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
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos clickedPos = context.getClickedPos();

            serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, clickedPos, Block.getId(state));
            KineticBlockEntity.switchToBlockState(serverLevel, clickedPos, AllBlocks.SHAFT.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, state.getValue(HORIZONTAL_AXIS)));

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
