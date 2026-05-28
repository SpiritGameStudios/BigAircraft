package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.gui.AllIcons;
import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class BigAircraftIcons extends AllIcons {

    private static final ResourceLocation ICON_ATLAS = BigAircraft.id("textures/gui/icons.png");
    private static final int ICON_ATLAS_SIZE = 64;
    private static final int ICON_SIZE = 16;

    private static int x = 0, y = -1;

    /**
     * mechanical speaker value selector
     */
    public static final BigAircraftIcons
        TEXT_ONLY = newRow(),
        BOTH = next(),
        SPEECH_ONLY = next();

    private final int iconX;
    private final int iconY;

    public BigAircraftIcons(int x, int y) {
        super(x, y);
        this.iconX = x * ICON_SIZE;
        this.iconY = y * ICON_SIZE;
    }

    private static BigAircraftIcons next() {
        return new BigAircraftIcons(++x, y);
    }

    private static BigAircraftIcons newRow() {
        return new BigAircraftIcons(x = 0, ++y);
    }

    @Override
    public void bind() {
        RenderSystem.setShaderTexture(0, ICON_ATLAS);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(ICON_ATLAS, x, y, 0, this.iconX, this.iconY, ICON_SIZE, ICON_SIZE, ICON_ATLAS_SIZE, ICON_ATLAS_SIZE);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource source, int color) {
        VertexConsumer builder = source.getBuffer(RenderType.text(ICON_ATLAS));
        PoseStack.Pose pose = stack.last();

        float minU = (float) this.iconX / ICON_ATLAS_SIZE;
        float maxU = (float) (this.iconX + ICON_SIZE) / ICON_ATLAS_SIZE;
        float minV = (float) this.iconY / ICON_ATLAS_SIZE;
        float maxV = (float) (this.iconY + ICON_SIZE) / ICON_ATLAS_SIZE;

        this.vertex(builder, pose, 0, 0, minU, minV, color);
        this.vertex(builder, pose, 0, 1, minU, maxV, color);
        this.vertex(builder, pose, 1, 1, maxU, maxV, color);
        this.vertex(builder, pose, 1, 0, maxU, minV, color);
    }

    private void vertex(VertexConsumer builder, PoseStack.Pose pose, int x, int y, float u, float v, int color) {
        builder.addVertex(pose, x, y, 0.0F).setColor(FastColor.ARGB32.opaque(color)).setUv(u, v).setLight(LightTexture.FULL_BRIGHT);
    }

}
