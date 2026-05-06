package dev.spiritstudios.aerobig;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.config.BigAircraftConfigService;
import dev.spiritstudios.aerobig.registry.ModBlockEntityTypes;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import dev.spiritstudios.aerobig.registry.ModItems;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import java.util.List;
import java.util.function.Consumer;

@Mod(BigAircraft.MOD_ID)
@EventBusSubscriber(modid = BigAircraft.MOD_ID)
public class BigAircraft {

    public static final String MOD_ID = "aerobig";
    public static final String MOD_NAME = "Big Aircraft";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final NonNullSupplier<SimulatedRegistrate> REGISTRATE = NonNullSupplier.lazy(
        () -> (SimulatedRegistrate) new SimulatedRegistrate(id(MOD_ID), MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
    );

    public BigAircraft(IEventBus modEventBus, ModContainer modContainer) {
        registrate().registerEventListeners(modEventBus);
        registrate().addRawLang(MOD_ID + ".simulated_section." + MOD_ID, MOD_NAME);

        ModBlocks.init();
        ModBlockEntityTypes.init();
        ModItems.init();

        BigAircraftConfigService.register(ModLoadingContext.get(), modContainer);
    }

    @SubscribeEvent
    public static void addBlockEntityBlocks(BlockEntityTypeAddBlocksEvent event) {
        addToExistingBlockEntity(event, AllBlockEntityTypes.GEARBOX, ModBlocks.CARBON_COMPOSITE_GEARBOXES);
        addToExistingBlockEntity(event, AllBlockEntityTypes.ENCASED_SHAFT, ModBlocks.CARBON_COMPOSITE_ENCASED_SHAFTS, ModBlocks.CARBON_COMPOSITE_WING_SHAFTS);
    }

    private static void addToExistingBlockEntity(BlockEntityTypeAddBlocksEvent event, BlockEntityEntry<? extends KineticBlockEntity> blockEntity, DyedBlockList<?>... dyedBlockLists) {
        List<BlockEntry<?>> dyedBlocks = Lists.newArrayList(Iterables.concat(dyedBlockLists));
        int length = dyedBlocks.size();

        Block[] blocks = new Block[length];

        for (int i = 0; i < length; i++)
            blocks[i] = dyedBlocks.get(i).get();

        event.modify(blockEntity.getKey(), blocks);
    }

    @SubscribeEvent
    public static void loadConfig(ModConfigEvent.Loading event) {
        forMatchingSpec(event, ConfigBase::onLoad);
    }

    @SubscribeEvent
    public static void reloadConfig(ModConfigEvent.Reloading event) {
        forMatchingSpec(event, ConfigBase::onReload);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (Mods.COMPUTERCRAFT.isLoaded())
            AnalogSpeedControllerBlockEntity.registerComputerBehaviour(event);
    }

    public static SimulatedRegistrate registrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void forMatchingSpec(ModConfigEvent event, Consumer<ConfigBase> action) {
        for (ConfigBase config : BigAircraftConfigService.CONFIGS.values()) {
            if (config.specification == event.getConfig().getSpec())
                action.accept(config);
        }
    }

}
