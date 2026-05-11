package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.component.ObservedAviationDisplaysComponent;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class ModDataComponents {

    public static final DataComponentType<List<UUID>> HUD_OBSERVERS = register(
        "hud_observers",
        builder -> builder
            .persistent(UUIDUtil.CODEC.listOf())
            .networkSynchronized(CatnipStreamCodecBuilders.list(UUIDUtil.STREAM_CODEC))
            .cacheEncoding()
    );

    public static final DataComponentType<ObservedAviationDisplaysComponent> OBSERVED_AVIATION_DISPLAYS = register(
        "observed_aviation_displays",
        builder -> builder
            .persistent(ObservedAviationDisplaysComponent.CODEC)
            .networkSynchronized(ObservedAviationDisplaysComponent.STREAM_CODEC)
            .cacheEncoding()
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, BigAircraft.id(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {}

}
