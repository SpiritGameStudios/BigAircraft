package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.AeronauticsBig;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ModItems {

    DeferredRegister.Items DEFERRED = DeferredRegister.createItems(AeronauticsBig.MODID);

    DeferredItem<BlockItem> CARBON_COMPOSITE = DEFERRED.registerSimpleBlockItem(
        "carbon_composite",
        ModBlocks.CARBON_COMPOSITE
    );

    static void register(IEventBus modEventBus) {
        DEFERRED.register(modEventBus);
    }

}
