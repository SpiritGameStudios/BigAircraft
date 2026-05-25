package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.velocity_sensor.VelocitySensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.client.render.Alignment;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.MonoNumberFont;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;
import org.joml.Vector3dc;

import static dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes.GUI_INVERT;
import static dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes.GUI_TEXTURED_INVERT;
import static dev.spiritstudios.aerobig.client.render.FlightHudRenderer.renderOutline;
import static dev.spiritstudios.aerobig.client.render.MonoNumberFont.BIG_BLOCK;
import static dev.spiritstudios.aerobig.client.render.MonoNumberFont.STOCK_SANS;

/**
 * TODO: move to new font system
 */
public class VelocitySensorFlightHudAugment extends FlightHudAugmentType<VelocitySensorBlockEntity> {
    private static final double BLOCKS_PER_TICK_TO_KNOTS = 18000.0 / 463.0;

    public VelocitySensorFlightHudAugment() {
        super(SimBlockEntityTypes.VELOCITY_SENSOR);
    }

    @Override
    public void render(VelocitySensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        FlightHudNumberRenderer stock = new FlightHudNumberRenderer(graphics, STOCK_SANS, GUI_TEXTURED_INVERT);
        FlightHudNumberRenderer big = new FlightHudNumberRenderer(graphics, BIG_BLOCK, GUI_TEXTURED_INVERT);

        float velocity = Math.abs(blockEntity.getAdjustedVelocity());

        int minX = graphics.guiWidth() / 4 - (6 + 12 + 4);
        int maxX = minX + 6;

        int maxY = graphics.guiHeight() / 4;
        int minY = graphics.guiHeight() - maxY;

        int vCentre = graphics.guiHeight() / 2;

        int outlineMinX = minX - (4 + 10 + STOCK_SANS.charWidth() * 3);

        big.alignTo(Alignment.LEFT);

        // number box
        int boxMax = vCentre + BIG_BLOCK.textureHeight() / 2 + 2;
        int boxMin = vCentre - BIG_BLOCK.textureHeight() / 2 - 3;

        graphics.hLine(GUI_INVERT, maxX - 1, outlineMinX, boxMax, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, maxX - 1, outlineMinX, boxMin, CommonColors.WHITE);
        graphics.vLine(GUI_INVERT, outlineMinX, boxMax, boxMin, CommonColors.WHITE);

        big.drawInt((int)velocity, outlineMinX + 4, vCentre - BIG_BLOCK.textureHeight() / 2);

        // outline
        graphics.hLine(GUI_INVERT, maxX, outlineMinX, minY + 1, CommonColors.WHITE);
        graphics.hLine(GUI_INVERT, maxX, outlineMinX, maxY - 1, CommonColors.WHITE);
        graphics.vLine(GUI_INVERT, maxX, minY + 1, maxY - 1, CommonColors.WHITE);

        stock.alignTo(Alignment.RIGHT);

        graphics.enableScissor(0, boxMax + 1, graphics.guiWidth(), minY);
        boolean switchedScissor = false;

        for (int i = 0; i < 1000; i += 10) {
            int y = vCentre - (int) ((i - velocity));

            if (!switchedScissor && y < boxMin - STOCK_SANS.textureHeight()) {
                graphics.disableScissor();
                graphics.enableScissor(0, maxY + 1, graphics.guiWidth(), boxMin);
                graphics.flush();
                switchedScissor = true;
            }

            graphics.hLine(GUI_INVERT, minX, maxX - 1, y, CommonColors.WHITE);

            if (i % 20 == 0) {
                stock.drawInt(i, minX - 4, y - STOCK_SANS.textureHeight() / 2);
            }
        }

        graphics.disableScissor();
    }
}
