package dev.spiritstudios.aerobig.flight_hud.augment_types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;

/**
 * TODO: fix cancellation of the previous augment renderer. something to do with popPose maybe???
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

        while (i < 0) {
            i += 360;
        }

        return i;
    }

    @Override
    public void render(GimbalSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        Pose3dc pose = subLevel.renderPose(partialTick);

        Vector3f angles = blockEntity.getBaseQuaternion().premul((float) pose.orientation().x(), (float) pose.orientation().y(), (float) pose.orientation().z(), (float) pose.orientation().w())
                .getEulerAnglesYXZ(new Vector3f());

        int windowCentreX = graphics.guiWidth() / 2;
        int windowCentreY = graphics.guiHeight() / 2;

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, MonoNumberFont.STOCK_SANS, BigAircraftRenderTypes.GUI_TEXTURED_INVERT);

        transformAndRenderLadder(graphics, graphics.pose(), numberRenderer, windowCentreX, windowCentreY, angles);
        renderHeading(graphics, mc, numberRenderer, angles, windowCentreX);
    }

    private static void transformAndRenderLadder(GuiGraphics graphics, PoseStack pose, FlightHudNumberRenderer numberRenderer, int windowCentreX, int windowCentreY, Vector3fc angles) {
        pose.pushPose();

        pose.translate(windowCentreX, windowCentreY, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(Mth.wrapDegrees(angles.z() * Mth.RAD_TO_DEG)));
        pose.translate(-windowCentreX, -windowCentreY, 0);

        int marginX = graphics.guiWidth() / 4;
        int marginY = graphics.guiHeight() / 4;

        FlightHudRenderer.scissor(graphics, marginX, marginY, guiGraphics -> renderLadder(guiGraphics, numberRenderer, angles.x(), angles.z(), windowCentreX, windowCentreY));

        pose.popPose();
    }

    private static void renderHeading(GuiGraphics graphics, Minecraft mc, FlightHudNumberRenderer numberRenderer, Vector3fc angles, int windowCentreX) {
        float yawDegrees = (angles.y() * Mth.RAD_TO_DEG + 180) % 360;

        int left = graphics.guiWidth() / 3;
        int right = left * 2;
        int topOffset = graphics.guiHeight() / 8;
        int degreesPerPixel = graphics.guiWidth() / mc.options.fov().get();

        int offset = windowCentreX - Mth.floor(yawDegrees * degreesPerPixel);

        numberRenderer.alignTo(Alignment.CENTER);
        numberRenderer.setPaddedZeroPlaces(3);

        for (int degrees = -540; degrees < 540; degrees++) {
            int x = degrees * degreesPerPixel + offset;

            if (x < left || x > right)
                continue;

            int length = getNotchLength(degrees);
            int halfLength = length / 2;

            if (degrees % DEGREE_INCREMENT == 0) {
                numberRenderer.drawInt(wrapHeading(degrees), x, topOffset + halfLength);
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
            float radians = degrees * Mth.DEG_TO_RAD - pitch;
            float magnitude = radians / Mth.PI * LADDER_SPACING;

            int up = (int) (-magnitude * graphics.guiHeight() + windowCentreY - 1);
            int length = degrees == 0 ? ZERO_RUNG_LENGTH : NORMAL_RUNG_LENGTH;

            hLine(graphics, windowCentreX - LADDER_OFFSET_FROM_CENTER, up, -length);
            hLine(graphics, windowCentreX + LADDER_OFFSET_FROM_CENTER - 1, up, length);

            int y = up - MonoNumberFont.STOCK_SANS.textureHeight() / 2;

//            graphics.pose().pushPose();
//            graphics.pose().rotateAround(
//                    Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)),
//                    windowCentreX - length - numberRenderer.getIntWidth(-degrees) - 12,
//                    y,
//                    0
//            );
            numberRenderer.alignTo(Alignment.RIGHT);
            numberRenderer.drawInt(-degrees, windowCentreX - length - LADDER_OFFSET_FROM_CENTER, y);
//            graphics.pose().popPose();

//            graphics.pose().pushPose();
//            graphics.pose().rotateAround(
//                    Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)),
//                    windowCentreX + length + numberRenderer.getIntWidth(-degrees) - 12,
//                    y,
//                    0
//            );
            numberRenderer.alignTo(Alignment.LEFT);
            numberRenderer.drawInt(-degrees, windowCentreX + length + LADDER_OFFSET_FROM_CENTER, y);
//            graphics.pose().popPose();
        }
    }

    private static void vLine(GuiGraphics graphics, int x, int y, int length) {
        graphics.vLine(BigAircraftRenderTypes.GUI_INVERT, x, y, y + length, CommonColors.WHITE);
    }

    private static void hLine(GuiGraphics graphics, int x, int y, int length) {
        graphics.hLine(BigAircraftRenderTypes.GUI_INVERT, x, x + length, y, CommonColors.WHITE);
    }
}
