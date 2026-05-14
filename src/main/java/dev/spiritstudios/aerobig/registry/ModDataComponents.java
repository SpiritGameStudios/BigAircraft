package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.component.FlightHudAugmentsComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public final class ModDataComponents {
    private static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.create(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            BigAircraft.MOD_ID
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FlightHudAugmentsComponent>> FLIGHT_HUD_AUGMENTS = register(
        "aviation_displays",
        builder -> builder
            .persistent(FlightHudAugmentsComponent.CODEC)
            .networkSynchronized(FlightHudAugmentsComponent.STREAM_CODEC)
            .cacheEncoding()
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builder
    ) {
        return REGISTER.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void init(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}
