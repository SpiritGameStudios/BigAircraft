package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.item.VerticalCarbonCompositeGearboxItem;
import dev.spiritstudios.aerobig.util.DyedItemList;

import static dev.spiritstudios.aerobig.BigAircraft.registrate;
import static dev.spiritstudios.aerobig.registry.ModBlocks.DEFAULT_WHITE_NAME;

public interface ModItems {

    DyedItemList<VerticalCarbonCompositeGearboxItem> VERTICAL_GEARBOXES = new DyedItemList<>(color -> {
        String path = DEFAULT_WHITE_NAME.apply(color, "vertical_carbon_composite_gearbox");

        return registrate().item(path, properties -> new VerticalCarbonCompositeGearboxItem(properties, color))
            .model((context, provider) -> provider
                .withExistingParent(path, provider.modLoc("block/wrapped_gearbox_item_vertical"))
                .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_gearbox")))
                .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
            )
            .register();
    });

    static void init() {}

}
