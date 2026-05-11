package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public record AeroGraphics(Minecraft mc, GuiGraphics graphics) {

    private static final double BPT_TO_KN = 18000.0 / 463.0;
    private static final int HORIZON_LINE_LENGTH = 30;
    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public void hLine(float x, float length, float y) {
        this.fill(x, y, x + length, y + 1);
    }

    public void vLine(float x, float length, float y) {
        this.fill(x, y, x + 1, y + length);
    }

    public void fill(float minX, float minY, float maxX, float maxY) {
        if (maxX > minX) {
            float prevMinX = minX;
            minX = maxX;
            maxX = prevMinX;
        }

        if (maxY > minY) {
            float prevMinY = minY;
            minY = maxY;
            maxY = prevMinY;
        }

        if (minX < 0 && maxX > graphics.guiWidth()) return;
        if (minY < 0 && maxY > graphics.guiHeight()) return;

        RenderSystem.setShader(GameRenderer::getPositionShader);

        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION
        );

        Matrix4f matrix = this.graphics.pose().last().pose();

        bufferBuilder.addVertex(matrix, minX, minY, 0);
        bufferBuilder.addVertex(matrix, minX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, minY, 0);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

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
        final float topOffset = 50;
        int degPerPixel = (graphics.guiWidth() / mc.options.fov().get());

        int offset = (graphics.guiWidth() / 2) - Mth.floor(heading * degPerPixel);

        for (int i = -540; i < 540; i ++) {
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
                        (int)topOffset + (len / 2) + 2,
                        CommonColors.WHITE
                );
            }

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            this.vLine(
                    x,
                    len,
                    topOffset - (len / 2F)
            );
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    public void drawAttitude(float pitch, float roll, int windowHeight, int windowCentreX, int windowCentreY) {
        graphics.pose().pushPose();
        graphics.pose().translate(windowCentreX, windowCentreY, 0);
        graphics.pose().mulPose(Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)));
        graphics.pose().translate(-windowCentreX, -windowCentreY, 0);

        var padX = graphics.guiWidth() / 4;
        var padY = graphics.guiHeight() / 4;

        graphics.enableScissor(
                padX, padY,
                graphics.guiWidth() - padX, graphics.guiHeight() - padY

        );

        final int step = 1;
        final float scale = 4;

        for (int i = -360; i < 360; i += step) {
            float angle = (i * Mth.DEG_TO_RAD) + pitch;
            float mag = angle / Mth.PI;
            mag *= scale;

            float up = -mag * windowHeight + windowCentreY - 1;

            int len = i % 5 == 0 ? 40 : 15;
            if (i == 0) len = 80;

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            this.hLine((float) windowCentreX - HORIZON_LINE_CENTRE_PADDING, -len, up);
            this.hLine((float) windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1, len, up);

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();

            var mc = Minecraft.getInstance();
            var str = String.format("%d", -i);
            if (i % 5 == 0) {
                this.write(
                        mc, str,
                        windowCentreX - len - 12 - mc.font.width(str),
                        (int) up - (mc.font.lineHeight / 2)
                );

                this.write(
                        mc, str,
                        windowCentreX + len + 12,
                        (int) up - (mc.font.lineHeight / 2)
                );
            }
        }
        graphics.disableScissor();
        graphics.pose().popPose();
    }

    public void drawAirspeed(Minecraft minecraft, int windowCentreY, Pose3dc pose, Pose3dc prevPose) {
        var dx = pose.position().x() - prevPose.position().x();
//        var dy =  pose.position().y() - prevPose.position().y();
        var dz = pose.position().z() - prevPose.position().z();

        var airspeedBPT = Math.sqrt(dx * dx + dz * dz);

        this.write(minecraft, "" + (int) (airspeedBPT * BPT_TO_KN), 0, windowCentreY + 10);
    }

    public void writeAltitude(Minecraft minecraft, int windowWidth, int windowCenterY, Vec3 pos) {
        Vec3 vec3 = Sable.HELPER.projectOutOfSubLevel(minecraft.level, pos);
        this.write(minecraft, "G%.2f".formatted(vec3.y), windowWidth - windowWidth / 4, windowCenterY - ALTITUDE_TEXT_HEIGHT / 2);
    }

    private void write(Minecraft minecraft, String text, int x, int y) {
        this.write(minecraft, Component.literal(text), x, y);
    }

    private void write(Minecraft minecraft, Component text, int x, int y) {
        this.graphics.drawString(minecraft.font, text, x, y, CommonColors.WHITE);
    }
}
