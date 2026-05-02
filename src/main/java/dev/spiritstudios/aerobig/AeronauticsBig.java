package dev.spiritstudios.aerobig;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.eriksonn.aeronautics.registry.AeroRegistrate;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.spiritstudios.aerobig.registry.AerospaceBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.AerospaceBlocks;
import dev.spiritstudios.aerobig.registry.AerospaceRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(AeronauticsBig.MODID)
public class AeronauticsBig {
    public static final String MODID = "aerobig";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final NonNullSupplier<AerospaceRegistrate> REGISTRATE = NonNullSupplier.lazy(() -> (AerospaceRegistrate) new AerospaceRegistrate(
            id("aerospace"),
            "aerobig"
    ).defaultCreativeTab((ResourceKey<CreativeModeTab>) null));

    public AeronauticsBig(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.get().registerEventListeners(modEventBus);
        AerospaceBlocks.init();
        AerospaceBlockEntityTypes.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
