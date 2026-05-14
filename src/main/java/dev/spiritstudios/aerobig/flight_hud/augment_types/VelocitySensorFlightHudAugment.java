package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.velocity_sensor.VelocitySensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
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

        final int xOffset = graphics.guiWidth() / 6;
        final int yOffset = graphics.guiHeight() / 2;

        String str = String.format("%d", (int) (airspeedBPT * BLOCKS_PER_TICK_TO_KNOTS));
        int strLen = mc.font.width("000");

        graphics.hLine(
                BigAircraftRenderTypes.GUI_INVERT,
                xOffset - 5, xOffset + strLen + 5,
                yOffset + mc.font.lineHeight + 5,
                CommonColors.WHITE
        );

        graphics.vLine(
                BigAircraftRenderTypes.GUI_INVERT,
                xOffset - 5,
                yOffset + mc.font.lineHeight + 5, yOffset - 3 - 5,
                CommonColors.WHITE
        );

        graphics.vLine(
                BigAircraftRenderTypes.GUI_INVERT,
                xOffset + strLen + 5,
                yOffset + mc.font.lineHeight + 5, yOffset - 3 - 5,
                CommonColors.WHITE
        );

        graphics.hLine(
                BigAircraftRenderTypes.GUI_INVERT,
                xOffset - 5, xOffset + strLen + 5,
                yOffset - 3 - 5,
                CommonColors.WHITE
        );

        graphics.drawString(
                mc.font,
                str,
                xOffset, yOffset,
                CommonColors.WHITE
        );
    }
}
