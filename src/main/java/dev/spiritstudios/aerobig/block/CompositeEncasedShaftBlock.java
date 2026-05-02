package dev.spiritstudios.aerobig.block;

import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import dev.spiritstudios.aerobig.registry.AerospaceBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CompositeEncasedShaftBlock extends EncasedShaftBlock implements SpecialBlockItemRequirement {
    public CompositeEncasedShaftBlock(Properties properties) {
        super(properties, AerospaceBlocks.CARBON_COMPOSITE);
    }

    @Override
    public Block getCasing() {
        return AerospaceBlocks.CARBON_COMPOSITE.get();
    }

    @Override
    public void handleEncasing(final BlockState state, final Level level, final BlockPos pos, final ItemStack heldItem, final Player player, final InteractionHand hand, final BlockHitResult ray) {
        super.handleEncasing(state, level, pos, heldItem, player, hand, ray);
        if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
        }
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return AerospaceBlockEntityTypes.CARBON_COMPOSITE_ENCASED_SHAFT.get();
    }
}
