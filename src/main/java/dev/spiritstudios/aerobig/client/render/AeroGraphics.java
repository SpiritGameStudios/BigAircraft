package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class AeroGraphics {
    public final GuiGraphics graphics;

    public AeroGraphics(GuiGraphics graphics) {
        this.graphics = graphics;
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

        Matrix4f matrix = graphics.pose().last().pose();

        bufferBuilder.addVertex(matrix, minX, minY, 0);
        bufferBuilder.addVertex(matrix, minX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, minY, 0);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}
