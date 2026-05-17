package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public final static class Blocks {
        public static final TagKey<Block> CARBON_COMPOSITE = create("carbon_composite");
        public static final TagKey<Block> CARBON_COMPOSITE_GEARBOXES = create("carbon_composite_gearboxes");
        public static final TagKey<Block> CARBON_COMPOSITE_ENCASED_SHAFTS = create("carbon_composite_encased_shafts");
        public static final TagKey<Block> CARBON_COMPOSITE_WINGS = create("carbon_composite_wings");
        public static final TagKey<Block> CARBON_COMPOSITE_WING_SHAFTS = create("carbon_composite_wing_shafts");
        public static final TagKey<Block> CARBON_COMPOSITE_STABILIZERS = create("carbon_composite_stabilizers");

        public static TagKey<Block> create(String path) {
            return ModTags.create(Registries.BLOCK, path);
        }
    }

    public final static class Items {
        public static final TagKey<Item> CARBON_COMPOSITE = create("carbon_composite");
        public static final TagKey<Item> CARBON_COMPOSITE_GEARBOXES = create("carbon_composite_gearboxes");
        public static final TagKey<Item> CARBON_COMPOSITE_WINGS = create("carbon_composite_wings");
        public static final TagKey<Item> CARBON_COMPOSITE_STABILIZERS = create("carbon_composite_stabilizers");

        public static TagKey<Item> create(String path) {
            return ModTags.create(Registries.ITEM, path);
        }
    }

    public static <T> TagKey<T> create(ResourceKey<Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, BigAircraft.id(path));
    }
}
