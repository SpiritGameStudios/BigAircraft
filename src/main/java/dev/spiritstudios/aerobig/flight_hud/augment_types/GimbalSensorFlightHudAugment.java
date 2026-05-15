package dev.spiritstudios.aerobig.flight_hud.augment_types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.client.render.*;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import org.joml.Vector3d;

/**
 * TODO: fix cancellation of all subsequent augment renderers. something to do with popPose maybe???
 */
public class GimbalSensorFlightHudAugment extends FlightHudAugmentType<GimbalSensorBlockEntity> {
    private static final int LADDER_OFFSET_FROM_CENTER = 7;
    private static final int DEGREE_INCREMENT = 5;

    private static final int NORMAL_RUNG_LENGTH = 15;
    private static final int ZERO_RUNG_LENGTH = 40;
    private static final int LADDER_SPACING = 3;

    private static final int NORMAL_HEADING_NOTCH_LENGTH = 6;
    private static final int INCREMENT_HEADING_NOTCH_LENGTH = 12;
    private static final int NINETY_DEGREE_HEADING_NOTCH_LENGTH = 18;
    private static final int HEADING_TEXT_VERTICAL_PADDING = 1;

    public GimbalSensorFlightHudAugment() {
        super(SimBlockEntityTypes.GIMBAL_SENSOR);
    }

    private static int wrapHeading(int angle) {
        int i = angle % 360;

        while (i < 0)
            i += 360;

        return i;
    }

    @Override
    public void render(GimbalSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        Vector3d forwardNormal = JOMLConversion.atLowerCornerOf(Direction.NORTH.getNormal());
        subLevel.renderPose(partialTick).orientation().transformInverse(forwardNormal);

        int windowCentreX = graphics.guiWidth() / 2;
        int windowCentreY = graphics.guiHeight() / 2;

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, NumericalFont.STOCK_SANS, BigAircraftRenderTypes.GUI_TEXTURED_INVERT);

        transformAndRenderLadder(graphics, graphics.pose(), numberRenderer, windowCentreX, windowCentreY, (float) blockEntity.getZAngle(), (float) blockEntity.getXAngle());
        renderHeading(graphics, mc, numberRenderer, forwardNormal, windowCentreX);
    }

    private static void transformAndRenderLadder(GuiGraphics graphics, PoseStack pose, FlightHudNumberRenderer numberRenderer, int windowCentreX, int windowCentreY, float roll, float pitch) {
        pose.pushPose();

        pose.translate(windowCentreX, windowCentreY, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)));
        pose.translate(-windowCentreX, -windowCentreY, 0);

        FlightHudRenderer.scissor(graphics, graphics.guiWidth() / 4, graphics.guiHeight() / 4, () -> renderLadder(graphics, numberRenderer, pitch, roll, windowCentreX, windowCentreY));

        pose.popPose();
    }

    private static void renderHeading(GuiGraphics graphics, Minecraft mc, FlightHudNumberRenderer numberRenderer, Vector3d forwardNormal, int windowCentreX) {
        float yawDegrees = (getYaw(forwardNormal) * Mth.RAD_TO_DEG + 180) % 360;

        int left = graphics.guiWidth() / 3;
        int right = left * 2;
        int topOffset = graphics.guiHeight() / 8;
        int degreesPerPixel = graphics.guiWidth() / mc.options.fov().get();

        int offset = windowCentreX - Mth.floor(yawDegrees * degreesPerPixel);

        for (int degrees = -540; degrees < 540; degrees++) {
            int x = degrees * degreesPerPixel + offset;

            if (x < left || x > right)
                continue;

            int length = getNotchLength(degrees);
            int halfLength = length / 2;

            if (degrees % DEGREE_INCREMENT == 0) {
                numberRenderer.drawInt(wrapHeading(degrees), x, topOffset + halfLength + HEADING_TEXT_VERTICAL_PADDING, Alignment.CENTER);
            }

            vLine(graphics, x, topOffset - halfLength, length);
        }
    }

    private static int getNotchLength(int angle) {
        if (angle % 90 == 0)
            return NINETY_DEGREE_HEADING_NOTCH_LENGTH;

        if (angle % DEGREE_INCREMENT == 0)
            return INCREMENT_HEADING_NOTCH_LENGTH;

        return NORMAL_HEADING_NOTCH_LENGTH;
    }

    private static void renderLadder(GuiGraphics graphics, FlightHudNumberRenderer numberRenderer, float pitch, float roll, int windowCentreX, int windowCentreY) {
        for (int degrees = -360; degrees < 360; degrees += DEGREE_INCREMENT) {
            float radians = degrees * Mth.DEG_TO_RAD + pitch;
            float mag = radians / Mth.PI * LADDER_SPACING; // what is this an abbreviation of?

            int up = (int) (-mag * graphics.guiHeight() + windowCentreY - 1);
            int length = degrees == 0 ? ZERO_RUNG_LENGTH : NORMAL_RUNG_LENGTH;

            hLine(graphics, windowCentreX - LADDER_OFFSET_FROM_CENTER, up, -length);
            hLine(graphics, windowCentreX + LADDER_OFFSET_FROM_CENTER - 1, up, length);

            int y = up - numberRenderer.font.atlasHeight / 2;

            graphics.pose().pushPose();
            graphics.pose().rotateAround(
                    Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)),
                    windowCentreX - length - numberRenderer.getWidth(-degrees) - 12,
                    y,
                    0
            );
            numberRenderer.drawInt(-degrees, windowCentreX - length - 12, y, Alignment.RIGHT);
            graphics.pose().popPose();

            graphics.pose().pushPose();
            graphics.pose().rotateAround(
                    Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)),
                    windowCentreX + length + numberRenderer.getWidth(-degrees) - 12,
                    y,
                    0
            );
            numberRenderer.drawInt(-degrees, windowCentreX + length + 12, y, Alignment.LEFT);
            graphics.pose().popPose();


        }
    }

    private static void vLine(GuiGraphics graphics, int x, int y, int length) {
        graphics.vLine(BigAircraftRenderTypes.GUI_INVERT, x, y, y + length, CommonColors.WHITE);
    }

    private static void hLine(GuiGraphics graphics, int x, int y, int length) {
        graphics.hLine(BigAircraftRenderTypes.GUI_INVERT, x, x + length, y, CommonColors.WHITE);
    }

    private static float getYaw(Vector3d forwardNormal) {
        float yaw = 0.0F;

        if (forwardNormal.x * forwardNormal.x > Mth.EPSILON) {
            yaw = (float) -Mth.atan2(-forwardNormal.x, forwardNormal.z) + Mth.PI;
        }

        return yaw;
    }

}
