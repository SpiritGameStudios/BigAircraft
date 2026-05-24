package dev.spiritstudios.aerobig.registry;

import dev.simulated_team.simulated.util.SimColors;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.block.speaker.MechanicalSpeakerBlockEntity;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public final class ModI18N {

    public static final MutableComponent SIMULATED_SECTION = addRaw("simulated_section." + BigAircraft.MOD_ID, BigAircraft.MOD_NAME);
    public static final MutableComponent SPEAKING_MODE = addRaw("speaking_mode", "Speaking Mode");
    public static final MutableComponent AUGMENTS = addRaw("flight_hud_augment.tooltip_heading", "Augments");

    public static MutableComponent flightHudAugment(FlightHudAugmentType<?> augmentType) {
        return Component.translatable(Util.makeDescriptionId("flight_hud_augment", ModBuiltInRegistries.FLIGHT_HUD_AUGMENTS.getKey(augmentType)));
    }

    private static MutableComponent addRaw(String path, String value) {
        return registrate().addRawLang(ofId(path), value);
    }

    public static String ofId(String path) {
        return BigAircraft.id(path).toLanguageKey();
    }

    public enum FlightHudAugmentError {

        ALREADY_OBSERVING("Flight HUD has already been augmented by this block!"),
        NOT_IN_SIMULATED_CONTRAPTION("This block is not in a Simulated Contraption!"),
        UNOBSERVABLE_INSTRUMENT("This block's info cannot be added to the flight HUD!");

        private final MutableComponent text;

        FlightHudAugmentError(String value) {
            this.text = registrate().addRawLang("flight_hud_augment.error." + this.name().toLowerCase(Locale.ROOT), value);
        }

        public MutableComponent getText() {
            return this.text.withColor(SimColors.NUH_UH_RED);
        }

        private static void init() {}

    }

    public static void init() {
        FlightHudAugmentError.init();
        MechanicalSpeakerBlockEntity.SpeakingMode.registerLang();
    }

}
