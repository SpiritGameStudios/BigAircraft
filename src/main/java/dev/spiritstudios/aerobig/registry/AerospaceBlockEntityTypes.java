package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.spiritstudios.aerobig.AeronauticsBig;

public class AerospaceBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsBig.REGISTRATE.get();

    public static final BlockEntityEntry<KineticBlockEntity> CARBON_COMPOSITE_ENCASED_SHAFT = REGISTRATE
            .blockEntity("carbon_composite_encased_shaft", KineticBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::shaft, false)
            .validBlocks(AerospaceBlocks.CARBON_COMPOSITE_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();


    public static void init() {
    }
}
