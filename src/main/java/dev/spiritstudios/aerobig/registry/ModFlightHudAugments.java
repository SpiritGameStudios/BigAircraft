package dev.spiritstudios.aerobig.registry;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.flight_hud.augment_types.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModFlightHudAugments {

    private static final DeferredRegister<FlightHudAugmentType<?>> REGISTER = DeferredRegister.create(
        ModRegistries.FLIGHT_HUD_AUGMENT,
        BigAircraft.MOD_ID
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, GimbalSensorFlightHudAugment> GIMBAL_SENSOR = register(
        "gimbal_sensor",
        GimbalSensorFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, AltitudeSensorFlightHudAugment> ALTITUDE_SENSOR = register(
        "altitude_sensor",
        AltitudeSensorFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, VelocitySensorFlightHudAugment> VELOCITY_SENSOR = register(
        "velocity_sensor",
        VelocitySensorFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, HotAirBurnerFlightHudAugment> HOT_AIR_BURNER = register(
        "hot_air_burner",
        HotAirBurnerFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, SteamVentFlightHudAugment> STEAM_VENT = register(
        "steam_vent",
        SteamVentFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, PortableEngineFlightHudAugment> PORTABLE_ENGINE = register(
        "portable_engine",
        PortableEngineFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, OpticalSensorFlightHudAugment> OPTICAL_SENSOR = register(
        "optical_sensor",
        OpticalSensorFlightHudAugment::new
    );

    public static final DeferredHolder<FlightHudAugmentType<?>, DockingConnectorFlightHudAugment> DOCKING_CONNECTOR = register(
        "docking_connector",
        DockingConnectorFlightHudAugment::new
    );

    private static <T extends FlightHudAugmentType<?>> DeferredHolder<FlightHudAugmentType<?>, T> register(String path, Supplier<T> sup) {
        BigAircraft.registrate().addLang("flight_hud_augment", BigAircraft.id(path), RegistrateLangProvider.toEnglishName(path));
        return REGISTER.register(path, sup);
    }

    public static void init(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}

