package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class ModBuiltInRegistries {
    public static final Registry<AviationDisplayType<?>> AVIATION_DISPLAY_TYPE = new RegistryBuilder<>(ModRegistries.AVIATION_DISPLAY_TYPE)
            .sync(true)
            .create();

    @SubscribeEvent
    private static void registerRegistries(NewRegistryEvent event) {
        event.register(AVIATION_DISPLAY_TYPE);
    }

    public static void init() {}

}
