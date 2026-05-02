package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import dev.spiritstudios.aerobig.AeronauticsBig;
import dev.spiritstudios.aerobig.block.CompositeEncasedShaftBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class AerospaceBlocks {
    private static final AerospaceRegistrate REGISTRATE = AeronauticsBig.REGISTRATE.get();

    public static final BlockEntry<Block> CARBON_COMPOSITE = REGISTRATE.block(
                    "carbon_composite",
                    Block::new
            )
            .properties(p -> p.mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.COPPER))
            .simpleItem()
            .register();

    public static final BlockEntry<CompositeEncasedShaftBlock> CARBON_COMPOSITE_ENCASED_SHAFT = REGISTRATE.block(
                    "carbon_composite_encased_shaft",
                    CompositeEncasedShaftBlock::new
            )
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p ->
                    p.mapColor(MapColor.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
            .transform(b -> b.transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT)))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
            .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
            .simpleItem()
            .register();

    public static void init() {
    }

}
