package dev.spiritstudios.aerobig.block;

import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.BlockHelper;
import dev.simulated_team.simulated.service.SimItemService;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.createmod.catnip.data.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeBlock extends CasingBlock implements SpecialBlockItemRequirement {

    private static final int MAX_TIMEOUT = 125;
    private static final BlockPos[] DIRECTION_OFFSETS = new BlockPos[] {
        new BlockPos(1, 0, 0),
        new BlockPos(-1, 0, 0),
        new BlockPos(0, 1, 0),
        new BlockPos(0, -1, 0),
        new BlockPos(0, 0, 1),
        new BlockPos(0, 0, -1),
        new BlockPos(1, 1, 0),
        new BlockPos(-1, -1, 0),
        new BlockPos(1, -1, 0),
        new BlockPos(-1, 1, 0),
        new BlockPos(1, 0, 1),
        new BlockPos(-1, 0, -1),
        new BlockPos(1, 0, -1),
        new BlockPos(-1, 0, 1),
        new BlockPos(0, 1, 1),
        new BlockPos(0, -1, -1),
        new BlockPos(0, -1, 1),
        new BlockPos(0, 1, -1)
    };

    protected final DyeColor color;

    public CarbonCompositeBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    private static void applyDye(BlockState state, Level level, BlockPos pos, DyeColor color) {
        if (tryApplySingularDye(level, pos, state, color) || trySpreadDyeToAdjacent(level, pos, color))
            return;

        List<BlockPos> frontier = new ObjectArrayList<>();
        frontier.add(pos);

        Set<BlockPos> visited = new ObjectOpenHashSet<>();

        BlockState newEnvelopeState = BlockHelper.copyProperties(state, AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(color).getDefaultState());
        BlockState newEncasedEnvelopeState = BlockHelper.copyProperties(state, AerospaceBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.get(color).getDefaultState());

        int timeout = MAX_TIMEOUT;

        while (!frontier.isEmpty() && timeout-- >= 0) {
            BlockPos currentPos = frontier.removeFirst();
            visited.add(currentPos);

            for (BlockPos blockPos : DIRECTION_OFFSETS) {
                BlockPos offsetPos = currentPos.offset(blockPos);

                if (visited.contains(offsetPos))
                    continue;

                BlockState adjacentState = level.getBlockState(offsetPos);

                if (!multiDye(level, offsetPos, adjacentState, newEnvelopeState) && !multiDye(level, offsetPos, adjacentState, newEncasedEnvelopeState))
                    continue;

                frontier.add(offsetPos);
                visited.add(offsetPos);
            }
        }
    }

    private static boolean trySpreadDyeToAdjacent(Level level, BlockPos pos, DyeColor color) {
        boolean hasDyed = false;

        for (Direction direction : Iterate.directions) {
            BlockPos offset = pos.relative(direction);
            BlockState adjacentState = level.getBlockState(offset);

            if (!tryApplySingularDye(level, offset, adjacentState, color))
                continue;

            hasDyed = true;
        }

        return hasDyed;
    }

    private static boolean tryApplySingularDye(Level level, BlockPos pos, BlockState state, DyeColor color) {
        if (state.getBlock() instanceof CarbonCompositeBlock carbonCompositeBlock && carbonCompositeBlock.color != color) {
            level.setBlockAndUpdate(pos, AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(color).getDefaultState());
            return true;
        }

        if (state.getBlock() instanceof CarbonCompositeEncasedShaftBlock encasedShaftBlock && encasedShaftBlock.color != color) {
            Direction.Axis axis = encasedShaftBlock.getRotationAxis(state);
            level.setBlockAndUpdate(pos, AerospaceBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.get(color).getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, axis));

            return true;
        }

        return false;
    }

    private static boolean multiDye(Level level, BlockPos pos, BlockState state, BlockState newState) {
        if (state.getBlock() instanceof CarbonCompositeBlock && newState.getBlock() instanceof CarbonCompositeBlock) {
            if (state != newState)
                level.setBlockAndUpdate(pos, newState);

            return true;
        }

        if (state.getBlock() instanceof CarbonCompositeEncasedShaftBlock && newState.getBlock() instanceof CarbonCompositeEncasedShaftBlock) {
            if (state != newState) {
                Direction.Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
                level.setBlockAndUpdate(pos, newState.setValue(RotatedPillarKineticBlock.AXIS, axis));
            }

            return true;
        }

        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useItemOn(itemStack, blockState, level, blockPos);
    }

    public static ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos) {
        DyeColor color = SimItemService.getDyeColor(itemStack);

        if (color != null) {
            if (!level.isClientSide())
                level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(level.getRandom(), 0.9F, 1.1F));

            applyDye(blockState, level, blockPos, color);

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(this.color).asStack();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, @Nullable BlockEntity blockEntity) {
        ItemStack stack = AerospaceBlocks.DYED_CARBON_COMPOSITE_BLOCKS.get(this.color).asStack();
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stack);
    }

}