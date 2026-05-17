package dev.spiritstudios.aerobig.registry;

import dev.eriksonn.aeronautics.Aeronautics;
import dev.eriksonn.aeronautics.data.AeroAdvancementTriggers;
import dev.simulated_team.simulated.data.advancements.SimulatedAdvancement;
import dev.spiritstudios.aerobig.BigAircraft;

public final class ModAdvancements {
    public static final SimulatedAdvancement ANALOG_SPEED_CONTROLLER = new SimulatedAdvancement(
            "analog_speed_controller",
            builder -> builder
                    .icon(ModBlocks.ANALOG_SPEED_CONTROLLER)
                    .title("Analog is the new digital")
                    .description("Fine-tune your Contraption with an Analog Rotation Speed Controller")
                    .special(SimulatedAdvancement.TaskType.NOISY),
            Aeronautics.path("textures/gui/advancement.png"),
            BigAircraft.MOD_ID,
            AeroAdvancementTriggers::addSimple
    );

    private ModAdvancements() {
    }
}
