package dev.spiritstudios.aerobig;

public interface LangKeys {

    String SIMULATED_SECTION = ofId("simulated_section." + BigAircraft.MOD_ID);
    // String ANALOG_SPEED_ACTUATOR_SPEED = ofId("kinetics.analog_speed_actuator.rotation_speed");

    private static String ofId(String path) {
        return BigAircraft.id(path).toLanguageKey();
    }

}
