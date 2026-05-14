package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ModRegistries {
    public static final ResourceKey<Registry<FlightHudAugmentType<?>>> FLIGHT_HUD_AUGMENT = key("flight_hud_augment");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(BigAircraft.id(name));
    }

    public static void init() {}

}
