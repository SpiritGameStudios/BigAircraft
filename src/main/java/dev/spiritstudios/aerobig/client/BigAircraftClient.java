package dev.spiritstudios.aerobig.client;

import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.client.render.FlightHudRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = BigAircraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = BigAircraft.MOD_ID, value = Dist.CLIENT)
public class BigAircraftClient {
    public BigAircraftClient(IEventBus modBus, ModContainer container) {
        modBus.register(FlightHudRenderer.class);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }
}
