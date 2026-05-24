package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.client.render.*;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

import static dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes.GUI_INVERT;
import static dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes.GUI_TEXTURED_INVERT;
import static dev.spiritstudios.aerobig.client.render.MonoNumberFont.BIG_BLOCK;
import static dev.spiritstudios.aerobig.client.render.MonoNumberFont.STOCK_SANS;

public class AltitudeSensorFlightHudAugment extends FlightHudAugmentType<AltitudeSensorBlockEntity> {

    public AltitudeSensorFlightHudAugment() {
        super(SimBlockEntityTypes.ALTITUDE_SENSOR);
    }

    @Override
    public void render(AltitudeSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        FlightHudNumberRenderer stock = new FlightHudNumberRenderer(graphics, STOCK_SANS, GUI_TEXTURED_INVERT);
        FlightHudNumberRenderer big = new FlightHudNumberRenderer(graphics, BIG_BLOCK, GUI_TEXTURED_INVERT);

        int left = graphics.guiWidth() - graphics.guiWidth() / 4;
        int right = graphics.guiWidth() - graphics.guiWidth() / 4 - 6;

        int top = graphics.guiHeight() / 4;
        int bottom = graphics.guiHeight() - top;

        int hCentre = graphics.guiHeight() / 2;

        int outlineL = left + 4 + 10 + STOCK_SANS.charWidth() * 3;
        int outlineR = right - 1;

        int boxT = hCentre + BIG_BLOCK.textureHeight() / 2 + 2;
        int boxB = hCentre - BIG_BLOCK.textureHeight() / 2 - 3;

        big.alignTo(Alignment.LEFT);
        big.drawDouble(blockEntity.getAirPressure(), outlineR + 4, bottom + STOCK_SANS.textureHeight());

        graphics.hLine(GUI_INVERT, outlineL, outlineR + 1, boxT, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, outlineL, outlineR + 1, boxB, CommonColors.WHITE);

        graphics.vLine(GUI_INVERT, outlineL, boxT, boxB, CommonColors.WHITE);

        big.drawInt((int) blockEntity.getWorldHeight(), outlineR + 4, boxT - 2);

        graphics.hLine(GUI_INVERT, right - 4, right - 12, hCentre, CommonColors.WHITE);

        graphics.hLine(GUI_INVERT, outlineL, outlineR, bottom + 1, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, outlineL, outlineR, top - 1, CommonColors.WHITE);

        graphics.vLine(GUI_INVERT, outlineR, bottom + 1, top - 1, CommonColors.WHITE);

        stock.alignTo(Alignment.LEFT);

        FlightHudRenderer.scissor(graphics, 0, boxT + 1, graphics.guiWidth(), bottom, guiGraphics -> drawAltitudeLadder(blockEntity, guiGraphics, level, stock, left, right));

        graphics.flush();

        FlightHudRenderer.scissor(graphics, 0, top, graphics.guiWidth(), boxB - 1, guiGraphics -> drawAltitudeLadder(blockEntity, guiGraphics, level, stock, left, right));
    }

    private static void drawAltitudeLadder(AltitudeSensorBlockEntity blockEntity, GuiGraphics graphics, ClientLevel level, FlightHudNumberRenderer numberRenderer, int left, int right) {
        for (int i = 0; i < level.getMaxBuildHeight(); i += 5) {
            int y = graphics.guiWidth() / 2 - (int) ((i - blockEntity.getWorldHeight()) * 4);

            graphics.hLine(GUI_INVERT, left, right, y, CommonColors.WHITE);

            if (i % 10 == 0)
                numberRenderer.drawInt(i, left + 4, y - STOCK_SANS.textureHeight() / 2);
        }
    }

}
