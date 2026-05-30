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

        int maxX = graphics.guiWidth() - graphics.guiWidth() / 4 + 6 + 12 + 4;
        int minX = maxX - 6;

        int maxY = graphics.guiHeight() / 4;
        int minY = graphics.guiHeight() - maxY;

        int vCentre = graphics.guiHeight() / 2;

        int outlineMaxX = maxX + 4 + 10 + STOCK_SANS.charWidth() * 3;

        big.alignTo(Alignment.LEFT);
        big.drawDouble(blockEntity.getAirPressure(), minX + 4, minY + STOCK_SANS.textureHeight());

        // number box
        int boxMax = vCentre + BIG_BLOCK.textureHeight() / 2 + 2;
        int boxMin = vCentre - BIG_BLOCK.textureHeight() / 2 - 3;

        graphics.hLine(GUI_INVERT, minX + 1, outlineMaxX, boxMax, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, minX + 1, outlineMaxX, boxMin, CommonColors.WHITE);
        graphics.vLine(GUI_INVERT, outlineMaxX, boxMax, boxMin, CommonColors.WHITE);

        big.drawInt((int) blockEntity.getWorldHeight(), minX + 4, vCentre - BIG_BLOCK.textureHeight() / 2);

        // outline
        graphics.hLine(GUI_INVERT, minX, outlineMaxX, minY + 1, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, minX, outlineMaxX, maxY - 1, CommonColors.WHITE);
        graphics.vLine(GUI_INVERT, minX, minY + 1, maxY - 1, CommonColors.WHITE);

        stock.alignTo(Alignment.LEFT);

        graphics.enableScissor(0, boxMax + 1, graphics.guiWidth(), minY);
        boolean switchedScissor = false;

        for (int i = 0; i < level.getMaxBuildHeight(); i += 5) {
            int y = vCentre - (int) ((i - blockEntity.getWorldHeight()) * 4);

            if (!switchedScissor && y < boxMin - STOCK_SANS.textureHeight()) {
                graphics.disableScissor();
                graphics.enableScissor(0, maxY, graphics.guiWidth(), boxMin);
                graphics.flush();
                switchedScissor = true;
            }

            graphics.hLine(GUI_INVERT, minX + 1, maxX, y, CommonColors.WHITE);

            if (i % 10 == 0) {
                stock.drawInt(i, maxX + 3, y - STOCK_SANS.textureHeight() / 2);
            }
        }

        graphics.disableScissor();
    }
}
