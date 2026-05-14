package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class ModBuiltInRegistries {
    public static final Registry<FlightHudAugmentType<?>> FLIGHT_HUD_AUGMENTS = new RegistryBuilder<>(ModRegistries.FLIGHT_HUD_AUGMENT)
            .sync(true)
            .create();

    @SubscribeEvent
    private static void registerRegistries(NewRegistryEvent event) {
        event.register(FLIGHT_HUD_AUGMENTS);
    }

    public static void init() {}

}
