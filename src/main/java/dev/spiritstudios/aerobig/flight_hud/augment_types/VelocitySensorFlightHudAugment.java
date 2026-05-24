package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.velocity_sensor.VelocitySensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.client.render.Alignment;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.MonoNumberFont;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;
import org.joml.Vector3dc;

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
        Vector3dc logicalVec = subLevel.logicalPose().position();
        Vector3dc prevVec = subLevel.lastPose().position();

        double dx = logicalVec.x() - prevVec.x();
        double dz = logicalVec.z() - prevVec.z();

        double airspeedBPT = Math.hypot(dx, dz);
        final int strLen = MonoNumberFont.STOCK_SANS.charWidth() * 3 - 1;

        final int padding = 2;

        final int minX = graphics.guiWidth() / 6;
        final int minY = graphics.guiHeight() / 2;

        final int maxX = minX + strLen - 1;
        final int maxY = minY + MonoNumberFont.STOCK_SANS.textureHeight() - 1;


        graphics.hLine(
                BigAircraftRenderTypes.GUI_INVERT,
                minX - padding, maxX + padding,
                minY - padding,
                CommonColors.WHITE
        );

        graphics.hLine(
                BigAircraftRenderTypes.GUI_INVERT,
                minX - padding, maxX + padding,
                maxY + padding,
                CommonColors.WHITE
        );

        graphics.vLine(
                BigAircraftRenderTypes.GUI_INVERT,
                minX - padding,
                minY - padding, maxY + padding,
                CommonColors.WHITE
        );

        graphics.vLine(
                BigAircraftRenderTypes.GUI_INVERT,
                maxX + padding,
                minY - padding, maxY + padding,
                CommonColors.WHITE
        );

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, MonoNumberFont.STOCK_SANS, BigAircraftRenderTypes.GUI_TEXTURED_INVERT);

        numberRenderer.alignTo(Alignment.LEFT);
        numberRenderer.drawInt(
                (int) (airspeedBPT * BLOCKS_PER_TICK_TO_KNOTS),
                minX, minY
        );
    }
}
