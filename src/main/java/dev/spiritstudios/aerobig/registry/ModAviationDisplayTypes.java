package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.aviation_display.types.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModAviationDisplayTypes {
    private static final DeferredRegister<AviationDisplayType<?>> REGISTER = DeferredRegister.create(
            ModRegistries.AVIATION_DISPLAY_TYPE,
            BigAircraft.MOD_ID
    );

    public static final DeferredHolder<AviationDisplayType<?>, GimbalSensorAviationDisplay> GIMBAL_SENSOR = REGISTER.register(
            "gimbal_sensor",
            GimbalSensorAviationDisplay::new
    );
    public static final DeferredHolder<AviationDisplayType<?>, AltitudeSensorAviationDisplay> ALTITUDE_SENSOR = REGISTER.register(
            "altitude_sensor",
            AltitudeSensorAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, VelocitySensorAviationDisplay> VELOCITY_SENSOR = REGISTER.register(
            "velocity_sensor",
            VelocitySensorAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, HotAirBurnerAviationDisplay> HOT_AIR_BURNER = REGISTER.register(
            "hot_air_burner",
            HotAirBurnerAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, SteamVentAviationDisplay> STEAM_VENT = REGISTER.register(
            "steam_vent",
            SteamVentAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, PortableEngineAviationDisplay> PORTABLE_ENGINE = REGISTER.register(
            "portable_engine",
            PortableEngineAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, OpticalSensorAviationDisplay> OPTICAL_SENSOR = REGISTER.register(
            "optical_sensor",
            OpticalSensorAviationDisplay::new
    );

    public static final DeferredHolder<AviationDisplayType<?>, DockingConnectorAviationDisplay> DOCKING_CONNECTOR = REGISTER.register(
            "docking_connector",
            DockingConnectorAviationDisplay::new
    );

    public static void init() {
    }
}
