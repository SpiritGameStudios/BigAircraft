package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.AeronauticsBig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

public interface ModBlocks {

    DeferredBlock<Block> CARBON_COMPOSITE = AeronauticsBig.BLOCKS.registerBlock(
        "carbon_composite",
        Block::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER)
    );

    static void init() {}

}
