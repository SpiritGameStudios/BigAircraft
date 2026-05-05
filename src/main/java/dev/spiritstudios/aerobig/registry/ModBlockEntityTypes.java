package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerRenderer;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public interface ModBlockEntityTypes {

    BlockEntityEntry<KineticBlockEntity> CARBON_COMPOSITE_ENCASED_SHAFT = registrate()
        .blockEntity("carbon_composite_encased_shaft", KineticBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual::shaft, false)
        .validBlocks(ModBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.toArray())
        .renderer(() -> ShaftRenderer::new)
        .register();

    BlockEntityEntry<AnalogSpeedControllerBlockEntity> ANALOG_SPEED_CONTROLLER = registrate()
        .blockEntity("analog_speed_controller", AnalogSpeedControllerBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual::shaft)
        .validBlocks(ModBlocks.ANALOG_SPEED_CONTROLLER)
        .renderer(() -> AnalogSpeedControllerRenderer::new)
        .register();

    static void init() {}

}
