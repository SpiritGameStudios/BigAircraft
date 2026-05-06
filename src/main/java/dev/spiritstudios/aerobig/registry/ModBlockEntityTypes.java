package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerRenderer;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public interface ModBlockEntityTypes {

    BlockEntityEntry<AnalogSpeedControllerBlockEntity> ANALOG_SPEED_CONTROLLER = registrate()
        .blockEntity("analog_speed_controller", AnalogSpeedControllerBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual::shaft)
        .validBlocks(ModBlocks.ANALOG_SPEED_CONTROLLER)
        .renderer(() -> AnalogSpeedControllerRenderer::new)
        .register();

    static void init() {}

}
