package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class ModBuiltInRegistries {

    public static final Registry<AviationDisplayType<?>> AVIATION_DISPLAY_TYPE = register(ModRegistries.AVIATION_DISPLAY_TYPE);

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        Registry<T> registry = new RegistryBuilder<>(key).sync(true).create();
        //noinspection unchecked
        ((WritableRegistry<Registry<T>>) BuiltInRegistries.REGISTRY).register(key, registry, RegistrationInfo.BUILT_IN);

        return registry;
    }

    public static void init() {}

}
