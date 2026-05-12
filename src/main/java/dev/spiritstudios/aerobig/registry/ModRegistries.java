package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ModRegistries {
    public static final ResourceKey<Registry<AviationDisplayType<?>>> AVIATION_DISPLAY_TYPE = key("aviation_display_type");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(BigAircraft.id(name));
    }

    public static void init() {}

}
