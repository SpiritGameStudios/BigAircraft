package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.math.Axis;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import org.joml.Vector3d;

public class GimbalSensorAviationDisplay extends AviationDisplayType {

    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int LADDER_STEP_DIST = 5;

    public GimbalSensorAviationDisplay() {
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
    public void render(GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!(level.getBlockEntity(blockPos) instanceof GimbalSensorBlockEntity gimbal)) return;

        float roll = (float) gimbal.getXAngle();
        float pitch = (float) gimbal.getZAngle();
        Vector3d forwardNormal = JOMLConversion.atLowerCornerOf(Direction.NORTH.getNormal());
        subLevel.logicalPose().orientation().transformInverse(forwardNormal);
        float yaw =  forwardNormal.x * forwardNormal.x > Mth.EPSILON ? (float) -Mth.atan2(-forwardNormal.x, forwardNormal.z) + Mth.PI : 0.0F;

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

        final int step = 5;
        final float scale = 3;

        for (int i = -360; i < 360; i += step) {
            float angle = (i * Mth.DEG_TO_RAD) + pitch;
            float mag = angle / Mth.PI;
            mag *= scale;

            int up = (int)(-mag * graphics.guiHeight() + windowCentreY - 1);

            int len = i % 5 == 0 ? 40 : 15;
            if (i == 0) len = 80;

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

            String str = String.format("%d", -i);
            if (i % 5 == 0) {
                int y = up - (mc.font.lineHeight / 2);

                graphics.drawString(mc.font, str, windowCentreX - len - 12 - mc.font.width(str), y, CommonColors.WHITE);
                graphics.drawString(mc.font, str, windowCentreX + len + 12, y,  CommonColors.WHITE);
            }
        }

        graphics.disableScissor();
        graphics.pose().popPose();

        float heading = ((yaw * Mth.RAD_TO_DEG) + 180) % 360;

        int left = graphics.guiWidth() / 3;
        int right = (graphics.guiWidth() / 3) * 2;
        final int topOffset = graphics.guiHeight() / 8;
        int degPerPixel = (graphics.guiWidth() / mc.options.fov().get());

        int offset = (graphics.guiWidth() / 2) - Mth.floor(heading * degPerPixel);

        for (int i = -540; i < 540; i++) {
            int x = (i * degPerPixel) + offset;
            if (x < left) continue;
            if (x > right) continue;

            int len = i % 5 == 0 ? 12 : 6;
            if (i % 90 == 0) len = 18;

            if (i % 5 == 0) {
                graphics.drawCenteredString(
                        mc.font,
                        String.format("%03d", wrapHeading(i)),
                        x,
                        topOffset + (len / 2) + 2,
                        CommonColors.WHITE
                );
            }

            graphics.vLine(
                    BigAircraftRenderTypes.GUI_INVERT,
                    x,
                    topOffset - (len / 2),
                    topOffset - (len / 2) + len,
                    CommonColors.WHITE
            );
        }
    }

}
