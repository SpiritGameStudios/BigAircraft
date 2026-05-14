package dev.spiritstudios.aerobig.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import dev.spiritstudios.aerobig.block.*;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import dev.spiritstudios.aerobig.config.BigAircraftStress;
import dev.spiritstudios.aerobig.item.VerticalCarbonCompositeGearboxItem;
import dev.spiritstudios.aerobig.mixin.CreativeTabItemTransformsAccessor;
import dev.spiritstudios.aerobig.util.OrderedDyedEntryList;
import dev.spiritstudios.aerobig.util.ModSpriteShifts;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;

public final class ModBlocks {
    public static final NonNullBiFunction<DyeColor, String, String> DEFAULT_WHITE_NAME = (color, path) -> color == DyeColor.WHITE ? path : color.getSerializedName() + '_' + path;
    public static final NonNullBiFunction<DyeColor, BlockBehaviour.Properties, BlockBehaviour.Properties> DEFAULT_CARBON_COMPOSITE_PROPERTIES = (color, properties) -> properties
            .mapColor(color.getMapColor())
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER);

    public static final OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeBlock>> CARBON_COMPOSITE_BLOCKS = new OrderedDyedEntryList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite");

        return registrate().block(path, properties -> new CarbonCompositeBlock(properties, color))
                .lang(RegistrateLangProvider.toEnglishName(path))
                .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties))
                .blockstate((context, provider) -> provider
                        .simpleBlock(context.get(), provider.models()
                                .cubeAll(path, provider.modLoc("block/" + path))
                        )
                )
                .onRegister(ModSpriteShifts.registerCasingCT(color, (state, direction) -> true))
                .tag(ModTags.Blocks.CARBON_COMPOSITE)
                .transform(TagGen.pickaxeOnly())
                .transform(CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY.conditionalApplyBlock(() -> color != DyeColor.WHITE))
                .item()
                .tag(ModTags.Items.CARBON_COMPOSITE)
                .build()
                .register();
    });

    public static final OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeEncasedShaftBlock>> CARBON_COMPOSITE_ENCASED_SHAFTS = new OrderedDyedEntryList<>(color -> {
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
                .loot((lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(block.getDyedVariants().get(color))
                        .withPool(lootTables.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                        ))
                ))
                .transform(BigAircraftStress.setNoImpact())
                .onRegister(ModSpriteShifts.registerCasingCT(color, ModSpriteShifts.AXES_MATCH_PREDICATE.negate()))
                .tag(ModTags.Blocks.CARBON_COMPOSITE_ENCASED_SHAFTS)
                .transform(TagGen.pickaxeOnly())
                .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
                .item()
                .build()
                .register();
    });

    public static final OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeWingBlock>> CARBON_COMPOSITE_WINGS = new OrderedDyedEntryList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing");

        return registrate().block(path, properties -> new CarbonCompositeWingBlock(properties, color))
                .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties))
                .blockstate((context, provider) -> provider.simpleBlock(context.get(), provider
                        .models()
                        .withExistingParent(path, provider.modLoc("block/template_wing"))
                        .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing_side")))
                        .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                ))
                .onRegister(ModSpriteShifts.registerCT(color, ModSpriteShifts.WingCTBehaviour::new))
                .tag(ModTags.Blocks.CARBON_COMPOSITE_WINGS)
                .transform(TagGen.pickaxeOnly())
                .transform(CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY.conditionalApplyBlock(() -> color != DyeColor.WHITE))
                .item()
                .tag(ModTags.Items.CARBON_COMPOSITE_WINGS)
                .build()
                .register();
    });

    public static final OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeWingShaftBlock>> CARBON_COMPOSITE_WING_SHAFTS = new OrderedDyedEntryList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing_shaft");

        return registrate().block(path, properties -> new CarbonCompositeWingShaftBlock(properties, color))
                .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties).noOcclusion())
                .blockstate(ModelConstructors.wingShaft(color))
                .loot((lootTables, block) -> lootTables.add(block, lootTables.createSingleItemTable(block.getDyedVariants().get(color))
                        .withPool(lootTables.applyExplosionCondition(AllBlocks.SHAFT.get(), LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(AllBlocks.SHAFT.get()))
                        ))
                ))
                .transform(BigAircraftStress.setNoImpact())
                .onRegister(ModSpriteShifts.registerCT(color, ModSpriteShifts.WingCTBehaviour::new))
                .tag(ModTags.Blocks.CARBON_COMPOSITE_WING_SHAFTS)
                .transform(TagGen.pickaxeOnly())
                .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                .transform(CreativeTabItemTransforms.VisibilityType.INVISIBLE.applyBlock())
                .item()
                .model((context, provider) -> provider
                        .withExistingParent(path, provider.modLoc("block/template_wing_shaft_item"))
                        .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing_side")))
                        .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                )
                .build()
                .register();
    });

    public static final OrderedDyedEntryList<Block, BlockEntry<CarbonCompositeGearboxBlock>> CARBON_COMPOSITE_GEARBOXES = new OrderedDyedEntryList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox");

        return registrate().block(path, properties -> new CarbonCompositeGearboxBlock(properties, color))
                .properties(properties -> DEFAULT_CARBON_COMPOSITE_PROPERTIES.apply(color, properties).noOcclusion())
                .onRegister(ModSpriteShifts.registerCasingCT(color, ModSpriteShifts.AXES_MATCH_PREDICATE))
                .tag(ModTags.Blocks.CARBON_COMPOSITE_GEARBOXES)
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
                .tag(ModTags.Items.CARBON_COMPOSITE_GEARBOXES)
                .model((context, provider) -> provider
                        .withExistingParent(path, provider.modLoc("block/wrapped_gearbox_item"))
                        .texture("side", provider.modLoc("block/" + path))
                        .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                )
                .build()
                .register();
    });

    /// This is here to make sure the vertical gearboxes are registered right after the horizontal ones, and as such appear in that order in the creative menu.
    public static final OrderedDyedEntryList<Item, ItemEntry<VerticalCarbonCompositeGearboxItem>> VERTICAL_GEARBOX_ITEMS = new OrderedDyedEntryList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "vertical_carbon_composite_gearbox");

        return registrate().item(path, properties -> new VerticalCarbonCompositeGearboxItem(properties, color))
                .transform(builder -> builder.onRegisterAfter(Registries.ITEM, item -> {
                    if (color != DyeColor.WHITE)
                        CreativeTabItemTransformsAccessor.getItemVisibility().put(item, CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY);
                }))
                .model((context, provider) -> provider
                        .withExistingParent(path, provider.modLoc("block/wrapped_gearbox_item_vertical"))
                        .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox")))
                        .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                )
                .tag(ModTags.Items.CARBON_COMPOSITE_GEARBOXES)
                .register();
    });

    public static final BlockEntry<Block> CARBON_PLATING = registrate()
            .block("carbon_plating", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BLACK))
            .transform(TagGen.pickaxeOnly())
            .blockstate(BlockStateGen.simpleCubeAll("carbon_plating"))
            .simpleItem()
            .register();

    public static final BlockEntry<StairBlock> CARBON_PLATING_STAIRS = registrate()
            .block("carbon_plating_stairs", properties -> new StairBlock(CARBON_PLATING.getDefaultState(), properties))
            .initialProperties(SharedProperties::softMetal)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BLACK))
            .transform(TagGen.pickaxeOnly())
            .blockstate((context, provider) -> provider.stairsBlock(context.get(), provider.modLoc("block/carbon_plating")))
            .recipe((context, provider) -> provider.stairs(DataIngredient.items(CARBON_PLATING.lazy()), RecipeCategory.BUILDING_BLOCKS, context, null, true))
            .tag(BlockTags.STAIRS)
            .item()
            .tag(ItemTags.STAIRS)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> CARBON_PLATING_SLAB = registrate()
            .block("carbon_plating_slab", SlabBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BLACK))
            .transform(TagGen.pickaxeOnly())
            .blockstate((context, provider) -> provider.slabBlock(context.get(), provider.modLoc("block/carbon_plating"), provider.modLoc("block/carbon_plating")))
            .recipe((context, provider) -> provider.slab(DataIngredient.items(CARBON_PLATING.lazy()), RecipeCategory.BUILDING_BLOCKS, context, null, true))
            .tag(BlockTags.SLABS)
            .item()
            .tag(ItemTags.SLABS)
            .build()
            .register();

    public static final BlockEntry<AnalogSpeedControllerBlock> ANALOG_SPEED_CONTROLLER = registrate()
            .block("analog_speed_controller", AnalogSpeedControllerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(properties -> properties.mapColor(MapColor.TERRACOTTA_YELLOW))
            .transform(TagGen.pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .transform(BigAircraftStress.setNoImpact())
            .blockstate(ModelConstructors.analogSpeedController())
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

    public static void init() {
    }

    private ModBlocks() {
    }
}
