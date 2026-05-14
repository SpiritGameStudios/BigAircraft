package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

/**
 * TODO: design entirely
 */
public class SteamVentFlightHudAugment extends FlightHudAugmentType<SteamVentBlockEntity> {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public SteamVentFlightHudAugment() {
        super(AeroBlockEntityTypes.STEAM_VENT);
    }

    @Override
    public void render(SteamVentBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!blockEntity.canOutputGas()) return;

        graphics.drawString(
                mc.font,
                "G%.2f".formatted(blockEntity.getGasOutput()),
                graphics.guiWidth() - graphics.guiWidth() / 4, graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2,
                CommonColors.WHITE
        );
    }
}
