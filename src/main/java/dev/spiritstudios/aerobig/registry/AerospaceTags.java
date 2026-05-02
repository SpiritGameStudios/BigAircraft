package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.AeronauticsBig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface AerospaceTags {

    interface Blocks {
        TagKey<Block> CARBON_COMPOSITE = create(Registries.BLOCK, "carbon_composite");
    }

    interface Items {
        TagKey<Item> CARBON_COMPOSITE = create(Registries.ITEM, "carbon_composite");
        TagKey<Item> SHAFTLESS_CARBON_COMPOSITE = create(Registries.ITEM, "shaftless_carbon_composite");
    }

    static <T> TagKey<T> create(ResourceKey<Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, AeronauticsBig.id(path));
    }

}
