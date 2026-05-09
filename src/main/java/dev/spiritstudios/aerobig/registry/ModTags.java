package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ModTags {

    interface Blocks {

        TagKey<Block> CARBON_COMPOSITE = create(Registries.BLOCK, "carbon_composite");
        TagKey<Block> CARBON_COMPOSITE_GEARBOXES = create(Registries.BLOCK, "carbon_composite_gearboxes");
        TagKey<Block> CARBON_COMPOSITE_ENCASED_SHAFTS = create(Registries.BLOCK, "carbon_composite_encased_shafts");
        TagKey<Block> CARBON_COMPOSITE_WINGS = create(Registries.BLOCK, "carbon_composite_wings");
        TagKey<Block> CARBON_COMPOSITE_WING_SHAFTS = create(Registries.BLOCK, "carbon_composite_wing_shafts");

    }

    interface Items {

        TagKey<Item> CARBON_COMPOSITE = create(Registries.ITEM, "carbon_composite");
        TagKey<Item> CARBON_COMPOSITE_GEARBOXES = create(Registries.ITEM, "carbon_composite_gearboxes");
        TagKey<Item> CARBON_COMPOSITE_WINGS = create(Registries.ITEM, "carbon_composite_wings");

    }

    static <T> TagKey<T> create(ResourceKey<Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, BigAircraft.id(path));
    }

}
