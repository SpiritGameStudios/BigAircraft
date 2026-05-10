package dev.spiritstudios.aerobig;

public final class LangKeys {
    public static final String SIMULATED_SECTION = ofId("simulated_section." + BigAircraft.MOD_ID);
    // String ANALOG_SPEED_ACTUATOR_SPEED = ofId("kinetics.analog_speed_actuator.rotation_speed");

    public static String ofId(String path) {
        return BigAircraft.id(path).toLanguageKey();
    }

}
