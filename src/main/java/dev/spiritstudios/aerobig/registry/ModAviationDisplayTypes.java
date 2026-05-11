package dev.spiritstudios.aerobig.registry;

import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.content.blocks.docking_connector.DockingConnectorBlockEntity;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import dev.simulated_team.simulated.content.blocks.lasers.optical_sensor.OpticalSensorBlockEntity;
import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineBlockEntity;
import dev.simulated_team.simulated.content.blocks.velocity_sensor.VelocitySensorBlockEntity;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.aviation_display.types.*;
import net.minecraft.core.Registry;

public final class ModAviationDisplayTypes {

    public static final AviationDisplayType<GimbalSensorBlockEntity> GIMBAL_SENSOR = register("gimbal_sensor", new GimbalSensorAviationDisplay());
    public static final AviationDisplayType<AltitudeSensorBlockEntity> ALTITUDE_SENSOR = register("altitude_sensor", new AltitudeSensorAviationDisplay());
    public static final AviationDisplayType<VelocitySensorBlockEntity> VELOCITY_SENSOR = register("velocity_sensor", new VelocitySensorAviationDisplay());
    public static final AviationDisplayType<HotAirBurnerBlockEntity> HOT_AIR_BURNER = register("hot_air_burner", new HotAirBurnerAviationDisplay());
    public static final AviationDisplayType<SteamVentBlockEntity> STEAM_VENT = register("steam_vent", new SteamVentAviationDisplay());
    public static final AviationDisplayType<PortableEngineBlockEntity> PORTABLE_ENGINE = register("portable_engine", new PortableEngineAviationDisplay());
    public static final AviationDisplayType<OpticalSensorBlockEntity> OPTICAL_SENSOR = register("optical_sensor", new OpticalSensorAviationDisplay());
    public static final AviationDisplayType<DockingConnectorBlockEntity> DOCKING_CONNECTOR = register("docking_connector", new DockingConnectorAviationDisplay());

    private static <T extends AviationDisplayType<?>> T register(String name, T type) {
        return Registry.register(ModBuiltInRegistries.AVIATION_DISPLAY_TYPE, BigAircraft.id(name), type);
    }

    public static void init() {}

}
