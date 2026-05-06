package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import dev.spiritstudios.aerobig.block.*;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import dev.spiritstudios.aerobig.config.BigAircraftStress;
import dev.spiritstudios.aerobig.util.ModSpriteShifts;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public interface ModBlocks {

    NonNullBiFunction<DyeColor, String, String> DEFAULT_WHITE_NAME = (color, path) -> color == DyeColor.WHITE ? path : color.getSerializedName() + '_' + path;
    NonNullBiFunction<DyeColor, BlockBehaviour.Properties, BlockBehaviour.Properties> DEFAULT_CARBON_COMPOSITE_PROPERTIES = (color, properties) -> properties
        .mapColor(color.getMapColor())
        .requiresCorrectToolForDrops()
        .strength(5.0F, 6.0F)
        .sound(SoundType.COPPER);

    DyedBlockList<CarbonCompositeBlock> CARBON_COMPOSITE_BLOCKS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite");

        return registrate().block(path, properties -> new CarbonCompositeBlock(properties, color))
            .lang(RegistrateLangProvider.toEnglishName(path))
            .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties))
            .blockstate((context, provider) -> provider
                .simpleBlock(context.get(), provider.models()
                    .cubeAll(path, provider.modLoc("block/" + path))
                )
            )
            .onRegister(registerCarbonCompositeCT(color))
            .tag(ModTags.Blocks.CARBON_COMPOSITE)
            .transform(TagGen.pickaxeOnly())
            .transform(CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY.conditionalApplyBlock(() -> color != DyeColor.WHITE))
            .item()
            .tag(ModTags.Items.CARBON_COMPOSITE)
            .tag(ModTags.Items.SHAFTLESS_CARBON_COMPOSITE)
            .build()
            .register();
    });

    DyedBlockList<CarbonCompositeEncasedShaftBlock> CARBON_COMPOSITE_ENCASED_SHAFTS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_encased_shaft");

        return registrate().block(path, properties -> new CarbonCompositeEncasedShaftBlock(properties, color))
            .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties).noOcclusion())
            .blockstate((context, provider) -> BlockStateGen.axisBlock(context, provider, blockState -> provider
                .models()
                .withExistingParent(path, provider.modLoc("block/template_carbon_composite_encased_shaft"))
                .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                .texture("gearbox", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox"))),
                true
            ))
            .loot((lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(block.dyedVariants().get(color))
                .withPool(lootTables.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                ))
            ))
            .onRegister(registerCarbonCompositeCT(color))
            .tag(ModTags.Blocks.CARBON_COMPOSITE)
            .transform(TagGen.pickaxeOnly())
            .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
            .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
            .item()
            .tag(ModTags.Items.CARBON_COMPOSITE)
            .build()
            .register();
    });

    DyedBlockList<CarbonCompositeWingBlock> CARBON_COMPOSITE_WINGS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing");

        return registrate().block(path, properties -> new CarbonCompositeWingBlock(properties, color))
            .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties))
            .blockstate((context, provider) -> provider.simpleBlock(context.get(), provider
                .models()
                .withExistingParent(path, provider.modLoc("block/template_carbon_composite_wing"))
                .texture("texture", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
            ))
            .tag(ModTags.Blocks.CARBON_COMPOSITE)
            .tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
            .transform(TagGen.pickaxeOnly())
            .transform(CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY.conditionalApplyBlock(() -> color != DyeColor.WHITE))
            .item()
            .tag(ModTags.Items.CARBON_COMPOSITE)
            .build()
            .register();
    });

    DyedBlockList<CarbonCompositeWingShaftBlock> CARBON_COMPOSITE_WING_SHAFTS = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing_shaft");

        return registrate().block(path, properties -> new CarbonCompositeWingShaftBlock(properties, color))
            .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties).noOcclusion())
            .blockstate((context, provider) -> provider.getVariantBuilder(context.get())
                .forAllStatesExcept(state -> {
                    Direction.Axis axis = state.getValue(CarbonCompositeWingShaftBlock.HORIZONTAL_AXIS);
                    String name = axis.getSerializedName();

                    return ConfiguredModel.builder()
                        .modelFile(provider.models()
                            .withExistingParent(path + '_' + name, provider.modLoc("block/template_carbon_composite_wing_shaft_" + name))
                            .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                            .texture("top", provider.modLoc("block/" + path))
                        )
                        .build();
                }, BlockStateProperties.WATERLOGGED)
            )
            .loot((lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(block.dyedVariants().get(color))
                .withPool(lootTables.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                ))
            ))
            .tag(ModTags.Blocks.CARBON_COMPOSITE)
            .tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
            .transform(TagGen.pickaxeOnly())
            .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
            .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
            .item()
            .model((context, provider) -> provider
                .withExistingParent(path, provider.modLoc("block/template_carbon_composite_wing_shaft_x"))
                .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                .texture("top", provider.modLoc("block/" + path))
            )
            .tag(ModTags.Items.CARBON_COMPOSITE)
            .build()
            .register();
    });

    DyedBlockList<CarbonCompositeGearboxBlock> CARBON_COMPOSITE_GEARBOXES = new DyedBlockList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox");

        return registrate().block(path, properties -> new CarbonCompositeGearboxBlock(properties, color))
            .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties).noOcclusion())
            .tag(ModTags.Blocks.CARBON_COMPOSITE)
            .transform(BigAircraftStress.setNoImpact())
            .transform(TagGen.pickaxeOnly())
            .transform(CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY.conditionalApplyBlock(() -> color != DyeColor.WHITE))
            .blockstate((context, provider) -> BlockStateGen.axisBlock(context, provider, blockState -> provider
                .models()
                .withExistingParent(path, provider.modLoc("block/wrapped_gearbox"))
                .texture("side", provider.modLoc("block/" + path))
                .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite"))),
                true
            ))
            .item()
            .tag(ModTags.Items.CARBON_COMPOSITE)
            .model((context, provider) -> provider
                .withExistingParent(path, provider.modLoc("block/wrapped_gearbox_item"))
                .texture("side", provider.modLoc("block/" + path))
                .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
            )
            .build()
            .register();
    });

    BlockEntry<AnalogSpeedControllerBlock> ANALOG_SPEED_CONTROLLER = registrate()
        .block("analog_speed_controller", AnalogSpeedControllerBlock::new)
        .initialProperties(SharedProperties::softMetal)
        .properties(properties -> properties.mapColor(MapColor.TERRACOTTA_YELLOW))
        .transform(TagGen.axeOrPickaxe())
        .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
        .transform(BigAircraftStress.setNoImpact())
        .blockstate((context, provider) -> provider
            .getVariantBuilder(context.get())
            .forAllStates(state -> {
                Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                return ConfiguredModel.builder()
                    .modelFile(provider
                        .models()
                        .getExistingFile(
                            provider.modLoc("block/" + context.getName() + (state.getValue(BlockStateProperties.POWERED) ? "_powered" : ""))
                        )
                    )
                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                    .build();
            })
        )
        .recipe((context, provider) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, context.get())
            .requires(CommonMetal.IRON.plates)
            .requires(AllItems.ELECTRON_TUBE)
            .requires(AllBlocks.ROTATION_SPEED_CONTROLLER)
            .unlockedBy("has_speed_controller", RegistrateRecipeProvider.has(AllBlocks.ROTATION_SPEED_CONTROLLER))
            .save(provider, context.getId())
        )
        .item()
        .model((context, provider) -> provider.blockItem(context::get, "_item"))
        .build()
        .register();

    private static NonNullConsumer<? super Block> registerCarbonCompositeCT(DyeColor color) {
        return CreateRegistrate.connectedTextures(() -> new SimpleCTBehaviour(ModSpriteShifts.omni(DEFAULT_WHITE_NAME.apply(color, "carbon_composite"))));
    }

    static void init() {
    }

}
