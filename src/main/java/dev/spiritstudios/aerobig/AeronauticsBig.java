package dev.spiritstudios.aerobig;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.spiritstudios.aerobig.registry.AerospaceBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(AeronauticsBig.MOD_ID)
public class AeronauticsBig {

    public static final String MOD_ID = "aerobig";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final NonNullSupplier<SimulatedRegistrate> REGISTRATE = NonNullSupplier.lazy(
        () -> (SimulatedRegistrate) new SimulatedRegistrate(id("aerospace"), MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
    );

    public AeronauticsBig(IEventBus modEventBus, ModContainer modContainer) {
        registrate().registerEventListeners(modEventBus);
        registrate().addRawLang("aerospace.simulated_section.aerospace", "Aerospace");

        AerospaceBlocks.init();
        AerospaceBlockEntityTypes.init();
    }

    public static SimulatedRegistrate registrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
