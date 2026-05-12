package dev.spiritstudios.aerobig.aviation_display.types;

import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class SteamVentAviationDisplay extends AviationDisplayType {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public SteamVentAviationDisplay() {
        super(AeroBlockEntityTypes.STEAM_VENT);
    }

    @Override
    public void render(GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!(level.getBlockEntity(blockPos) instanceof SteamVentBlockEntity vent)) return;
        if (!vent.canOutputGas()) return;

        graphics.drawString(
                mc.font,
                "G%.2f".formatted(vent.getGasOutput()),
                graphics.guiWidth() - graphics.guiWidth() / 4, graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2,
                CommonColors.WHITE
        );
    }
}
