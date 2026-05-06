package dev.spiritstudios.aerobig.registry;

import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import dev.spiritstudios.aerobig.item.VerticalCarbonCompositeGearboxItem;
import dev.spiritstudios.aerobig.mixin.CreativeTabItemTransformsAccessor;
import dev.spiritstudios.aerobig.util.DyedItemList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;
import static dev.spiritstudios.aerobig.registry.ModBlocks.DEFAULT_WHITE_NAME;

public interface ModItems {

    DyedItemList<VerticalCarbonCompositeGearboxItem> VERTICAL_GEARBOXES = new DyedItemList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "vertical_carbon_composite_gearbox");

        return registrate().item(path, properties -> new VerticalCarbonCompositeGearboxItem(properties, color))
            .transform(builder -> builder.onRegisterAfter(Registries.ITEM, item -> {
                if (color != DyeColor.WHITE)
                    CreativeTabItemTransformsAccessor.getItemVisibilities().put(item, CreativeTabItemTransforms.VisibilityType.SEARCH_ONLY);
            }))
            .model((context, provider) -> provider
                .withExistingParent(path, provider.modLoc("block/wrapped_gearbox_item_vertical"))
                .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox")))
                .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
            )
            .register();
    });

    static void init() {}

}
