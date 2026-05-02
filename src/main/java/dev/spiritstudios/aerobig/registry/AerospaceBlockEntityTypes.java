package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.spiritstudios.aerobig.AeronauticsBig;

public interface AerospaceBlockEntityTypes {

    BlockEntityEntry<KineticBlockEntity> CARBON_COMPOSITE_ENCASED_SHAFT = AeronauticsBig.registrate()
        .blockEntity("carbon_composite_encased_shaft", KineticBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual::shaft, false)
        .validBlocks(AerospaceBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS.toArray())
        .renderer(() -> ShaftRenderer::new)
        .register();

    static void init() {}

}
