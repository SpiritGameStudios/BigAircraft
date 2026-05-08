package dev.spiritstudios.aerobig.block;

import com.google.common.collect.Sets;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
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

public interface CarbonComposite<T extends Block & CarbonComposite<T>> {

    int MAX_TIMEOUT = 125;

    /**
     * All positions around an arbitrary point [0, 0, 0] creating a hollow 3x3x3 sphere. In other words, a 3x3x3 cube with the 8 vertices and the center missing.
     */
    HashSet<Vec3i> DYE_SPHERICAL_OFFSETS = Util.make(Sets.newHashSetWithExpectedSize(18), set -> {
        Vec3i vec;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    vec = new Vec3i(x, y, z);

                    if (!vec.equals(Vec3i.ZERO) && vec.distManhattan(Vec3i.ZERO) < 3)
                        set.add(vec);
                }
            }
        }
    });

    @NotNull DyeColor getDyeColor();
    @NotNull DyedBlockList<T> getDyedVariants();
    @NotNull DyeableGroup getDyeableGroup();

    default BlockEntry<T> getOfColor() {
        return this.getDyedVariants().get(this.getDyeColor());
    }

    default ItemRequirement getItemRequirement() {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.getOfColor().asStack());
    }

    static ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos) {
        DyeColor color = DyeColor.getColor(itemStack);

        if (color != null && applyDyeOn(blockState, level, blockPos, color)) {
            level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(level.getRandom(), 0.9F, 1.1F));
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean applyDyeOn(BlockState clickedState, Level level, BlockPos pos, DyeColor color) {
        return tryDye(level, pos, clickedState, clickedState, color) || trySpreadDyeToAdjacent(level, pos, clickedState, color) || trySpreadDyeIteratively(level, pos, clickedState, color);
    }

    private static boolean trySpreadDyeIteratively(Level level, BlockPos origin, BlockState clickedState, DyeColor color) {
        boolean hasDyed = false;

        List<BlockPos> frontier = new ObjectArrayList<>();
        Set<BlockPos> visited = new ObjectOpenHashSet<>();

        frontier.add(origin);
        int timeout = MAX_TIMEOUT;

        while (!frontier.isEmpty() && timeout-- >= 0) {
            BlockPos currentPos = frontier.removeFirst();
            visited.add(currentPos);

            for (Vec3i vec : DYE_SPHERICAL_OFFSETS) {
                BlockPos offsetPos = currentPos.offset(vec);

                if (visited.contains(offsetPos))
                    continue;

                BlockState offsetState = level.getBlockState(offsetPos);

                if (tryDye(level, offsetPos, clickedState, offsetState, color)) {
                    frontier.add(offsetPos);
                    visited.add(offsetPos);

                    hasDyed = true;
                }
            }
        }

        return hasDyed;
    }

    private static boolean trySpreadDyeToAdjacent(Level level, BlockPos pos, BlockState clickedState, DyeColor color) {
        boolean hasDyed = false;

        for (Direction direction : Direction.values()) {
            BlockPos offset = pos.relative(direction);
            BlockState offsetState = level.getBlockState(offset);

            if (tryDye(level, offset, clickedState, offsetState, color))
                hasDyed = true;
        }

        return hasDyed;
    }

    private static boolean tryDye(Level level, BlockPos applyingPos, BlockState clickedState, BlockState applyingState, DyeColor color) {
        if (applyingState.getBlock() instanceof CarbonComposite<?> carbonComposite && carbonComposite.getDyeColor() != color && carbonComposite.inDyeableGroup(clickedState)) {
            BlockState blockState = carbonComposite.getDyedVariants().get(color).getDefaultState();
            level.setBlockAndUpdate(applyingPos, BlockHelper.copyProperties(applyingState, blockState));

            return true;
        }

        return false;
    }

    private boolean inDyeableGroup(BlockState state) {
        return state.getBlock() instanceof CarbonComposite<?> carbonComposite && this.getDyeableGroup() == carbonComposite.getDyeableGroup();
    }

}
