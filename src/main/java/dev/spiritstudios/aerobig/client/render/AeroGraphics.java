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

public record AeroGraphics(GuiGraphics graphics) {

    private static final double BPT_TO_KN = 18000.0 / 463.0;
    private static final int HORIZON_LINE_LENGTH = 30;
    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public void line(float x, float length, float y) {
        this.fill(x, y, x + length, y + 1);
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

    public void drawAttitude(float pitch, float roll, int windowHeight, int windowCentreX, int windowCentreY) {
        graphics.pose().pushPose();
        graphics.pose().translate(windowCentreX, windowCentreY, 0);
        graphics.pose().mulPose(Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)));
        graphics.pose().translate(-windowCentreX, -windowCentreY, 0);

        final int step = 1;
        final float scale = 4;

        for (int i = -180; i < 180; i += step) {
            float angle = (i * Mth.DEG_TO_RAD) + pitch;
            float mag = angle / Mth.PI;
            mag *= scale;

            float up = -mag * windowHeight + windowCentreY - 1;

//            int up = i * windowHeight + windowCentreY - 1;

            int len = i % 5 == 0 ? 40 : 15;
            if (i == 0) len = 80;

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            this.line((float) windowCentreX - HORIZON_LINE_CENTRE_PADDING, -len, up);
            this.line((float) windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1, len, up);

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

        graphics.pose().popPose();
    }

    public void writeAirspeed(Minecraft minecraft, int windowCentreY, Pose3dc pose, Pose3dc prevPose) {
        double airSpeedBPT = pose.position().distance(prevPose.position());
        this.write(minecraft, "" + (int) airSpeedBPT * BPT_TO_KN, 0, windowCentreY + 10);
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
