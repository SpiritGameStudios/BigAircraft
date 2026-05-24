package dev.spiritstudios.aerobig.item;

import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ItemEvents {
    @SubscribeEvent
    private static void addTooltip(ItemTooltipEvent event) {
        event.getItemStack().addToTooltip(
                ModDataComponents.FLIGHT_HUD_AUGMENTS.get(),
                event.getContext(),
                event.getToolTip()::add,
                event.getFlags()
        );

    }
}
