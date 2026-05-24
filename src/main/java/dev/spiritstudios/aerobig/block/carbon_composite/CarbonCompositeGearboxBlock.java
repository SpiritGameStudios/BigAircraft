package dev.spiritstudios.aerobig.block.carbon_composite;

import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CarbonCompositeGearboxBlock extends GearboxBlock implements CarbonComposite<CarbonCompositeGearboxBlock> {

    private final DyeColor color;

    public CarbonCompositeGearboxBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return CarbonComposite.useItemOn(stack, state, level, pos, player, hand, hitResult, null);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(AXIS).isVertical())
            return super.getDrops(state, builder);

        return List.of(ModBlocks.VERTICAL_GEARBOX_ITEMS.get(this.getDyeColor()).asStack());
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (state.getValue(AXIS).isVertical())
            return super.getCloneItemStack(state, target, level, pos, player);

        return new ItemStack(ModBlocks.VERTICAL_GEARBOX_ITEMS.get(this.getDyeColor()).get());
    }

    @Override
    public DyeColor getDyeColor() {
        return this.color;
    }

    @Override
    public OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeGearboxBlock>> getDyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_GEARBOXES;
    }

    @Override
    public DyeableGroup getDyeableGroup() {
        return DyeableGroup.FUSELAGE;
    }

}
