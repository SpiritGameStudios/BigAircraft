package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public record AeroGraphics(Minecraft mc, GuiGraphics graphics) {
    public static final RenderStateShard.TransparencyStateShard INVERT = new RenderStateShard.TransparencyStateShard(
            BigAircraft.MOD_ID + ":invert",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO
                );
            },
            () -> {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
            }
    );

    public static final RenderType GUI_INVERT = RenderType.create(
            BigAircraft.MOD_ID + ":gui_invert",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            786432,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_GUI_SHADER)
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setTransparencyState(INVERT)
                    .createCompositeState(false)
    );

    private static final double BPT_TO_KN = 18000.0 / 463.0;
    private static final int HORIZON_LINE_LENGTH = 30;
    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    private static int wrapHeading(int angle) {
        int i = angle % 360;

        while (i < 0) {
            i += 360;
        }

        return i;
    }

    public void drawHeading(float yaw, float pitch, float roll, int windowHeight, int windowCentreX, int windowCentreY) {
        float heading = ((yaw * Mth.RAD_TO_DEG) + 180) % 360;

        int left = graphics.guiWidth() / 3;
        int right = (graphics.guiWidth() / 3) * 2;
        final int topOffset = 50;
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
                    GUI_INVERT,
                    x,
                    topOffset - (len / 2),
                    topOffset - (len / 2) + len,
                    CommonColors.WHITE
            );
        }
    }

    public void drawAttitude(float pitch, float roll, int windowHeight, int windowCentreX, int windowCentreY) {
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

            int up = (int)(-mag * windowHeight + windowCentreY - 1);

            int len = i % 5 == 0 ? 40 : 15;
            if (i == 0) len = 80;

            graphics.hLine(
                    GUI_INVERT,
                    (windowCentreX - HORIZON_LINE_CENTRE_PADDING) - len,
                    windowCentreX - HORIZON_LINE_CENTRE_PADDING,
                    up,
                    CommonColors.WHITE
            );

            graphics.hLine(
                    GUI_INVERT,
                    windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1,
                    (windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1) + len,
                    up,
                    CommonColors.WHITE
            );

            String str = String.format("%d", -i);
            if (i % 5 == 0) {
                int y = up - (mc.font.lineHeight / 2);

                this.write(str, windowCentreX - len - 12 - this.mc.font.width(str), y, false);
                this.write(str, windowCentreX + len + 12, y, false);
            }
        }

        graphics.disableScissor();
        graphics.pose().popPose();
    }

    public void drawAirspeed(int windowCentreY, Pose3dc pose, Pose3dc prevPose) {
        double dx = pose.position().x() - prevPose.position().x();
//        var dy =  pose.position().y() - prevPose.position().y();
        double dz = pose.position().z() - prevPose.position().z();

        double airspeedBPT = Math.hypot(dx, dz);

        this.write("" + (int) (airspeedBPT * BPT_TO_KN), 0, windowCentreY + 10, true);
    }

    public void writeAltitude(int windowWidth, int windowCenterY, Vec3 pos) {
        Vec3 vec3 = Sable.HELPER.projectOutOfSubLevel(this.mc.level, pos);
        this.write("G%.2f".formatted(vec3.y), windowWidth - windowWidth / 4, windowCenterY - ALTITUDE_TEXT_HEIGHT / 2, true);
    }

    private void write(String text, int x, int y, boolean dropShadow) {
        this.write(Component.literal(text), x, y, dropShadow);
    }

    private void write(Component text, int x, int y, boolean dropShadow) {
        this.graphics.drawString(this.mc.font, text, x, y, CommonColors.WHITE, dropShadow);
    }
}
