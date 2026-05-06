package dev.spiritstudios.aerobig.block;

import com.google.common.collect.Sets;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.service.SimItemService;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.createmod.catnip.data.Iterate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: verify new impl
 */
public interface CarbonComposite<T extends Block & CarbonComposite<T>> {

    /**
     * Add <code>CARBON_COMPOSITES.add(this);</code> to the constructor.
     */
    HashSet<CarbonComposite<?>> CARBON_COMPOSITES = Sets.newHashSet();

    int MAX_TIMEOUT = 125;

    /**
     * All positions around an arbitrary point [0, 0, 0] creating a hollow 3x3x3 sphere. In other words, a 3x3x3 cube with the 8 vertices and the center missing.
     */
    HashSet<Vec3i> DIRECTION_OFFSETS = Util.make(Sets.newHashSetWithExpectedSize(18), set -> {
        Vec3i vec;

        for (int x = -1; x < 1; x++) {
            for (int y = -1; y < 1; y++) {
                for (int z = -1; z < 1; z++) {
                    vec = new Vec3i(x, y, z);

                    if (!vec.equals(Vec3i.ZERO) && vec.distManhattan(Vec3i.ZERO) < 3)
                        set.add(vec);
                }
            }
        }
    });

    @NotNull
    DyeColor color();

    @NotNull
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
        Set<BlockPos> visited = new ObjectOpenHashSet<>();

        frontier.add(pos);
        int timeout = MAX_TIMEOUT;

        while (!frontier.isEmpty() && timeout-- >= 0) {
            BlockPos currentPos = frontier.removeFirst();
            visited.add(currentPos);

            for (Vec3i vec : DIRECTION_OFFSETS) {
                BlockPos offsetPos = currentPos.offset(vec);

                if (!visited.contains(offsetPos) && appliedMultiDyeToPos(level, offsetPos, state, color)) {
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

    private static boolean appliedMultiDyeToPos(Level level, BlockPos offsetPos, BlockState state, DyeColor color) {
        BlockState adjacentState = level.getBlockState(offsetPos);

        for (CarbonComposite<?> carbonComposite : CARBON_COMPOSITES) {
            if (tryMultiDye(level, offsetPos, adjacentState, BlockHelper.copyProperties(state, carbonComposite.dyedVariants().get(color).getDefaultState())))
                return true;
        }

        return false;
    }

    private static boolean tryMultiDye(Level level, BlockPos pos, BlockState state, BlockState newState) {
        if (!(state.getBlock() instanceof CarbonComposite<?> carbonComposite && carbonComposite.dyedVariants().contains(newState.getBlock())))
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
