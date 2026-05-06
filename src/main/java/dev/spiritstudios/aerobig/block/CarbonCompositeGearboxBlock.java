package dev.spiritstudios.aerobig.block;

import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.registry.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
        CARBON_COMPOSITES.add(this);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return CarbonComposite.useItemOn(itemStack, blockState, level, blockPos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (state.getValue(AXIS).isVertical())
            return super.getDrops(state, builder);

        return List.of(ModItems.VERTICAL_GEARBOXES.get(this.color()).asStack());
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (state.getValue(AXIS).isVertical())
            return super.getCloneItemStack(state, target, level, pos, player);

        return new ItemStack(ModItems.VERTICAL_GEARBOXES.get(this.color()).get());
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        super.onSneakWrenched(state, context);

        if (context.getLevel() instanceof ServerLevel) {
            Player player = context.getPlayer();

            if (player != null && !player.isCreative())
                player.getInventory().placeItemBackInInventory(this.getOfColor().asStack());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public DyeColor color() {
        return this.color;
    }

    @Override
    public DyedBlockList<CarbonCompositeGearboxBlock> dyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_GEARBOXES;
    }

}
