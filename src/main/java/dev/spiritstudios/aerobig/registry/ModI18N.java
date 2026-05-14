package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.network.chat.MutableComponent;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public final class ModI18N {

    public static final MutableComponent SIMULATED_SECTION = registrate().addRawLang(
        ofId("simulated_section." + BigAircraft.MOD_ID), BigAircraft.MOD_NAME
    );

    public static String ofId(String path) {
        return BigAircraft.id(path).toLanguageKey();
    }

    public static final class FlightHudAugment {

        public static final MutableComponent ERROR_ALREADY_OBSERVING = error(
            "already_observing",
            "Flight HUD has already been augmented by this block!"
        );

        public static final MutableComponent ERROR_NOT_IN_SIMULATED_CONTRAPTION = error(
            "not_in_simulated_contraption",
            "This block is not in a Simulated Contraption!"
        );

        public static final MutableComponent ERROR_UNOBSERVABLE_INSTRUMENT = error(
            "unobservable_instrument",
            "This block's info cannot be added to the flight HUD!"
        );

        private static MutableComponent error(String key, String value) {
            return registrate().addRawLang("flight_hud_augment.error." + key, value);
        }

        public static void init() {}

    }

    public static void init() {
        FlightHudAugment.init();
    }

}
