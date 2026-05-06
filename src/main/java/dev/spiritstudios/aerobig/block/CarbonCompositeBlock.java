package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeBlock extends CasingBlock implements CarbonComposite<CarbonCompositeBlock> {

    private final DyeColor color;

    public CarbonCompositeBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        CARBON_COMPOSITES.add(this);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return CarbonComposite.useItemOn(itemStack, blockState, level, blockPos);
    }

    @Override
    public DyeColor color() {
        return this.color;
    }

    @Override
    public DyedBlockList<CarbonCompositeBlock> dyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_BLOCKS;
    }

}