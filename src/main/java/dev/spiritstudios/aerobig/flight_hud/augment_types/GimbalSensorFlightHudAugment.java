package dev.spiritstudios.aerobig.flight_hud.augment_types;

import com.mojang.math.Axis;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import dev.spiritstudios.aerobig.client.render.NumericalFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import org.joml.Vector3d;

public class GimbalSensorFlightHudAugment extends FlightHudAugmentType<GimbalSensorBlockEntity> {

    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int LADDER_STEP_DIST = 5;
    private static final int ZERO_RUNG_LENGTH = 40;
    private static final int NORMAL_RUNG_LENGTH = 15;

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
        float roll = (float) blockEntity.getXAngle();
        float pitch = (float) blockEntity.getZAngle();
        Vector3d forwardNormal = JOMLConversion.atLowerCornerOf(Direction.NORTH.getNormal());
        subLevel.logicalPose().orientation().transformInverse(forwardNormal);

        int windowCentreX = graphics.guiWidth() / 2;
        int windowCentreY = graphics.guiHeight() / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(windowCentreX, windowCentreY, 0);
        graphics.pose().mulPose(Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)));
        graphics.pose().translate(-windowCentreX, -windowCentreY, 0);

        int padX = graphics.guiWidth() / 4;
        int padY = graphics.guiHeight() / 4;

        graphics.enableScissor(
                padX, padY,
                graphics.guiWidth() - padX, graphics.guiHeight() - padY
        );

        final float scale = 3;

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, NumericalFont.SMALL, BigAircraftRenderTypes.NUMBER_INVERT);

        for (int i = -360; i < 360; i += LADDER_STEP_DIST) {
            float angle = (i * Mth.DEG_TO_RAD) + pitch;
            float mag = angle / Mth.PI;
            mag *= scale;

            int up = (int)(-mag * graphics.guiHeight() + windowCentreY - 1);
            int len = i == 0 ? ZERO_RUNG_LENGTH : NORMAL_RUNG_LENGTH;

            graphics.hLine(
                    BigAircraftRenderTypes.GUI_INVERT,
                    (windowCentreX - HORIZON_LINE_CENTRE_PADDING) - len,
                    windowCentreX - HORIZON_LINE_CENTRE_PADDING,
                    up,
                    CommonColors.WHITE
            );

            graphics.hLine(
                    BigAircraftRenderTypes.GUI_INVERT,
                    windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1,
                    (windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1) + len,
                    up,
                    CommonColors.WHITE
            );

            int y = up - (mc.font.lineHeight / 2);

            numberRenderer.drawInt(-i, windowCentreX - len - 12, y, FlightHudNumberRenderer.Alignment.RIGHT);
            numberRenderer.drawInt(-i, windowCentreX + len + 12, y, FlightHudNumberRenderer.Alignment.LEFT);
        }

        graphics.disableScissor();
        graphics.pose().popPose();

        float heading = (getYaw(forwardNormal) * Mth.RAD_TO_DEG + 180) % 360;

        int left = graphics.guiWidth() / 3;
        int right = left * 2;
        final int topOffset = graphics.guiHeight() / 8;
        int degPerPixel = graphics.guiWidth() / mc.options.fov().get();

        int offset = windowCentreX - Mth.floor(heading * degPerPixel);

        for (int i = -540; i < 540; i++) {
            int x = (i * degPerPixel) + offset;
            if (x < left || x > right)
                continue;

            int len = i % 5 == 0 ? 12 : 6;
            if (i % 90 == 0) len = 18;

            if (i % 5 == 0)
                numberRenderer.drawInt(wrapHeading(i), x, topOffset + (len / 2) + 2, FlightHudNumberRenderer.Alignment.CENTER);

            graphics.vLine(
                    BigAircraftRenderTypes.GUI_INVERT,
                    x,
                    topOffset - (len / 2),
                    topOffset - (len / 2) + len,
                    CommonColors.WHITE
            );
        }
    }

    private static float getYaw(Vector3d forwardNormal) {
        float yaw = 0.0F;

        if (forwardNormal.x * forwardNormal.x > Mth.EPSILON)
            yaw = (float) -Mth.atan2(-forwardNormal.x, forwardNormal.z) + Mth.PI;

        return yaw;
    }

}
