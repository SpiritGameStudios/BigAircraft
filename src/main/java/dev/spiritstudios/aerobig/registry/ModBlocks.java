package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.spiritstudios.aerobig.AeronauticsBig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public interface ModBlocks {

    DeferredRegister.Blocks DEFERRED = DeferredRegister.createBlocks(AeronauticsBig.MODID);

    DeferredBlock<Block> CARBON_COMPOSITE = DEFERRED.registerBlock(
        "carbon_composite",
        Block::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER)
    );

    BlockEntry<EncasedShaftBlock> BRASS_ENCASED_SHAFT = AeronauticsBig.REGISTRATE
        .block("brass_encased_shaft", properties -> new EncasedShaftBlock(properties, ModBlocks.CARBON_COMPOSITE::get))
        .properties(properties -> properties.mapColor(MapColor.TERRACOTTA_BROWN))
        .transform(BuilderTransformers.encasedShaft("brass", () -> AllSpriteShifts.BRASS_CASING))
        .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
        .transform(pickaxeOnly())
        .register();

    DeferredBlock<EncasedShaftBlock> CARBON_COMPOSITE_ENCASED_SHAFT = DEFERRED.registerBlock(
        "carbon_composite_encased_shaft",
        properties -> new EncasedShaftBlock(properties, CARBON_COMPOSITE),
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER)
    );

    static void register(IEventBus modEventBus) {
        DEFERRED.register(modEventBus);
    }

}
