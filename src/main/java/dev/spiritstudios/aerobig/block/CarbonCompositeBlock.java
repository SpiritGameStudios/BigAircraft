package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.util.OrderedDyedEntryList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return CarbonComposite.useItemOn(stack, state, level, pos, player, hand, hitResult, null);
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
    }

    @Override
    public OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeBlock>> getDyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_BLOCKS;
    }

    @Override
    public DyeableGroup getDyeableGroup() {
        return DyeableGroup.FUSELAGE;
    }

}