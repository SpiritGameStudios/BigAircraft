package dev.spiritstudios.aerobig.registry;

import dev.eriksonn.aeronautics.Aeronautics;
import dev.eriksonn.aeronautics.data.AeroAdvancementTriggers;
import dev.eriksonn.aeronautics.index.AeroBlocks;
import dev.simulated_team.simulated.data.advancements.SimulatedAdvancement;
import dev.spiritstudios.aerobig.AeronauticsBig;

public class AerospaceAdvancements {
    public static final SimulatedAdvancement ANALOG_SPEED_CONTROLLER = new SimulatedAdvancement("analog_speed_controller", b -> b.icon(AeroBlocks.WHITE_ENVELOPE_BLOCK).title("Analog is the new digital").description("Fine-tune your Contraption with an Analog Rotation Speed Controller").special(SimulatedAdvancement.TaskType.NOISY), Aeronautics.path("textures/gui/advancement.png"), AeronauticsBig.MOD_ID, AeroAdvancementTriggers::addSimple);
}
