package dev.spiritstudios.aerobig;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AeronauticsBig.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AeronauticsBig.MOD_ID, value = Dist.CLIENT)
public class AeronauticsBigClient {

    public AeronauticsBigClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        AeronauticsBig.LOGGER.info("HELLO FROM CLIENT SETUP");
        AeronauticsBig.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

}
