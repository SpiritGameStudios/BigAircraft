package dev.spiritstudios.aerobig.block.carbon_composite;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.ryanhcode.sable.api.block.BlockSubLevelCustomCenterOfMass;
import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.util.OrderedDyedEntryList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3dc;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICarbonCompositeWing extends CarbonComposite<CarbonCompositeWingBlock>, BlockSubLevelLiftProvider, BlockSubLevelCustomCenterOfMass {

    @Override
    default OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeWingBlock>> getDyedVariants() {
        return ModBlocks.CARBON_COMPOSITE_WINGS;
    }

    @Override
    default DyeableGroup getDyeableGroup() {
        return DyeableGroup.CONTROL_SURFACE;
    }

    @Override
    default Direction sable$getNormal(final BlockState state) {
        return Direction.DOWN;
    }

    @Override
    default Vector3dc getCenterOfMass(final BlockGetter blockGetter, final BlockState state) {
        return JOMLConversion.HALF;
    }

}
