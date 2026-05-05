package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.service.SimItemService;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public interface CarbonComposite<T extends Block & CarbonComposite<?>> {

    int MAX_TIMEOUT = 125;
    BlockPos[] DIRECTION_OFFSETS = new BlockPos[] {
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

    DyeColor color();

    DyedBlockList<T> dyedVariants();

    default BlockEntry<T> getOfColor() {
        return this.dyedVariants().get(this.color());
    }

    default ItemRequirement getItemRequirement() {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.getOfColor().asStack());
    }

    static ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos) {
        DyeColor color = SimItemService.getDyeColor(itemStack);

        if (color != null) {
            if (!level.isClientSide())
                level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(level.getRandom(), 0.9F, 1.1F));

            applyDye(blockState, level, blockPos, color);

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static void applyDye(BlockState state, Level level, BlockPos pos, DyeColor color) {
        if (tryApplySingularDye(level, pos, state, color) || trySpreadDyeToAdjacent(level, pos, color))
            return;

        List<BlockPos> frontier = new ObjectArrayList<>();
        frontier.add(pos);

        Set<BlockPos> visited = new ObjectOpenHashSet<>();

        BlockState newEnvelopeState = BlockHelper.copyProperties(state, ModBlocks.CARBON_COMPOSITE_BLOCKS.get(color).getDefaultState());
        BlockState newEncasedEnvelopeState = BlockHelper.copyProperties(state, ModBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.get(color).getDefaultState());
        BlockState newGearboxState = BlockHelper.copyProperties(state, ModBlocks.CARBON_COMPOSITE_GEARBOXES.get(color).getDefaultState());

        int timeout = MAX_TIMEOUT;

        while (!frontier.isEmpty() && timeout-- >= 0) {
            BlockPos currentPos = frontier.removeFirst();
            visited.add(currentPos);

            for (BlockPos blockPos : DIRECTION_OFFSETS) {
                BlockPos offsetPos = currentPos.offset(blockPos);

                if (visited.contains(offsetPos))
                    continue;

                BlockState adjacentState = level.getBlockState(offsetPos);

                if (
                    multiDye(level, offsetPos, adjacentState, newEnvelopeState) ||
                    multiDye(level, offsetPos, adjacentState, newEncasedEnvelopeState) ||
                    multiDye(level, offsetPos, adjacentState, newGearboxState)
                ) {
                    frontier.add(offsetPos);
                    visited.add(offsetPos);
                }
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
        if (state.getBlock() instanceof CarbonComposite<?> carbonComposite && carbonComposite.color() != color) {
            BlockState blockState = carbonComposite.dyedVariants().get(color).getDefaultState();

            if (carbonComposite instanceof RotatedPillarKineticBlock rotatedPillar)
                blockState = blockState.setValue(RotatedPillarKineticBlock.AXIS, rotatedPillar.getRotationAxis(state));

            level.setBlockAndUpdate(pos, blockState);

            return true;
        }

        return false;
    }

    private static boolean multiDye(Level level, BlockPos pos, BlockState state, BlockState newState) {
        if (!(state.getBlock() instanceof CarbonComposite<?> carbonComposite) || !carbonComposite.dyedVariants().contains(newState.getBlock()))
            return false;

        if (state != newState) {
            if (carbonComposite instanceof RotatedPillarKineticBlock)
                newState = newState.setValue(RotatedPillarKineticBlock.AXIS, state.getValue(RotatedPillarKineticBlock.AXIS));

            level.setBlockAndUpdate(pos, newState);

            return true;
        }

        return false;
    }

}
