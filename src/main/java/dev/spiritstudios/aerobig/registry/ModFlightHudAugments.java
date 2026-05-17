package dev.spiritstudios.aerobig.registry;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.flight_hud.augment_types.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModFlightHudAugments {
    private static final DeferredRegister<FlightHudAugmentType<?>> REGISTER = DeferredRegister.create(
            ModRegistries.FLIGHT_HUD_AUGMENT,
            BigAircraft.MOD_ID
    );

    static {
        register("gimbal_sensor", GimbalSensorFlightHudAugment::new);
        register("altitude_sensor", AltitudeSensorFlightHudAugment::new);
        register("velocity_sensor", VelocitySensorFlightHudAugment::new);
        register("hot_air_burner", HotAirBurnerFlightHudAugment::new);
        register("steam_vent", SteamVentFlightHudAugment::new);
        register("portable_engine", PortableEngineFlightHudAugment::new);
        register("optical_sensor", OpticalSensorFlightHudAugment::new);
        register("docking_connector", DockingConnectorFlightHudAugment::new);
    }

    private static <T extends FlightHudAugmentType<?>> void register(String path, Supplier<T> sup) {
        REGISTER.register(path, sup);
        BigAircraft.registrate().addLang("flight_hud_augment", BigAircraft.id(path), RegistrateLangProvider.toEnglishName(path));
    }

    public static void init(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}
