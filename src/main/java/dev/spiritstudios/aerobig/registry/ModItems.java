package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.AeronauticsBig;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;

public interface ModItems {

    DeferredItem<BlockItem> CARBON_COMPOSITE = AeronauticsBig.ITEMS.registerSimpleBlockItem(
        "carbon_composite",
        ModBlocks.CARBON_COMPOSITE
    );

    static void init() {}

}
