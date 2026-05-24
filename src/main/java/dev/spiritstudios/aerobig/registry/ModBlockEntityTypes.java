package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerRenderer;
import dev.spiritstudios.aerobig.block.speaker.MechanicalSpeakerBlockEntity;
import dev.spiritstudios.aerobig.block.speaker.MechanicalSpeakerRenderer;
import dev.spiritstudios.aerobig.block.speaker.MechanicalSpeakerVisual;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public final class ModBlockEntityTypes {

    public static final BlockEntityEntry<AnalogSpeedControllerBlockEntity> ANALOG_SPEED_CONTROLLER = registrate()
        .blockEntity("analog_speed_controller", AnalogSpeedControllerBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual::shaft)
        .validBlocks(ModBlocks.ANALOG_SPEED_CONTROLLER)
        .renderer(() -> AnalogSpeedControllerRenderer::new)
        .register();

    public static final BlockEntityEntry<MechanicalSpeakerBlockEntity> MECHANICAL_SPEAKER = registrate()
        .blockEntity("mechanical_speaker", MechanicalSpeakerBlockEntity::new)
        .visual(() -> MechanicalSpeakerVisual::new)
        .validBlocks(ModBlocks.MECHANICAL_SPEAKER)
        .renderer(() -> MechanicalSpeakerRenderer::new)
        .register();

    public static void init() {
    }

    private ModBlockEntityTypes() {
    }
}
