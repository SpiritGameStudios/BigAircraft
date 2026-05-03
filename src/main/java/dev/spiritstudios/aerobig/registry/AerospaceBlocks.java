package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import dev.spiritstudios.aerobig.block.CarbonCompositeBlock;
import dev.spiritstudios.aerobig.block.CarbonCompositeEncasedShaftBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static dev.spiritstudios.aerobig.AeronauticsBig.registrate;

public interface AerospaceBlocks {

    NonNullBiFunction<DyeColor, String, String> DEFAULT_WHITE_NAME = (color, path) -> color == DyeColor.WHITE ? path : color.getSerializedName() + '_' + path;

    DyedBlockList<CarbonCompositeBlock> DYED_CARBON_COMPOSITE_BLOCKS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite");

        return registrate().block(path, properties -> new CarbonCompositeBlock(properties, color))
                .lang(RegistrateLangProvider.toEnglishName(path))
                .properties(properties -> properties
                        .mapColor(color.getMapColor())
                        .requiresCorrectToolForDrops()
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.COPPER)
                )
                .blockstate((context, provider) -> provider
                        .simpleBlock(context.get(), provider.models()
                                .cubeAll(path, provider.modLoc("block/" + path))
                        )
                )
                .tag(AerospaceTags.Blocks.CARBON_COMPOSITE)
                .transform(TagGen.pickaxeOnly())
                .item()
                .tag(AerospaceTags.Items.CARBON_COMPOSITE)
                .tag(AerospaceTags.Items.SHAFTLESS_CARBON_COMPOSITE)
                .build()
                .register();
    });

    DyedBlockList<CarbonCompositeEncasedShaftBlock> CARBON_COMPOSITE_ENCASED_SHAFTS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_encased_shaft");

        return registrate().block(path, properties -> new CarbonCompositeEncasedShaftBlock(properties, color))
                .properties(properties -> properties
                        .mapColor(color.getMapColor())
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.COPPER)
                )
                .blockstate((context, provider) -> BlockStateGen.axisBlock(context, provider, blockState -> provider
                                .models()
                                .withExistingParent(path, provider.modLoc("block/template_carbon_composite_encased_shaft"))
                                .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                                .texture("gearbox", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox"))),
                        true
                ))
                .loot((lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(DYED_CARBON_COMPOSITE_BLOCKS.get(color))
                        .withPool(lootTables.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                        ))
                ))
                .tag(AerospaceTags.Blocks.CARBON_COMPOSITE)
                .transform(TagGen.pickaxeOnly())
                .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
                .item()
                .tag(AerospaceTags.Items.CARBON_COMPOSITE)
                .build()
                .register();
    });

    BlockEntry<AnalogSpeedControllerBlock> ANALOG_SPEED_CONTROLLER = registrate()
            .block("analog_speed_controller", AnalogSpeedControllerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .register();

    static void init() {
    }

}
