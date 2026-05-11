package dev.spiritstudios.aerobig.aviation_display;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public abstract class AviationDisplayType<T extends BlockEntity> {

    public static final SimpleRegistry<BlockEntityType<?>, AviationDisplayType<?>> BY_BLOCK_ENTITY = SimpleRegistry.create();

    private final boolean displayWhenOutsideBESubLevel;

    public AviationDisplayType(NonNullSupplier<BlockEntityType<T>> blockEntityType, boolean displayWhenOutsideBESubLevel) {
        this.displayWhenOutsideBESubLevel = displayWhenOutsideBESubLevel;
        BY_BLOCK_ENTITY.register(blockEntityType.get(), this);
    }

    public boolean canDisplay(T blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return this.displayWhenOutsideBESubLevel || beSubLevel.getPlot().contains(pos);
    }

    public abstract void display(T blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick);

    public void hLine(GuiGraphics graphics, PoseStack poseStack, float x, float length, float y) {
        this.fill(graphics, poseStack, x, y, x + length, y + 1);
    }

    public void vLine(GuiGraphics graphics, PoseStack poseStack, float x, float length, float y) {
        this.fill(graphics, poseStack, x, y, x + 1, y + length);
    }

    public void fill(GuiGraphics graphics, PoseStack poseStack, float minX, float minY, float maxX, float maxY) {
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

        Matrix4f matrix = poseStack.last().pose();

        bufferBuilder.addVertex(matrix, minX, minY, 0);
        bufferBuilder.addVertex(matrix, minX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, maxY, 0);
        bufferBuilder.addVertex(matrix, maxX, minY, 0);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    public void write(Minecraft mc, GuiGraphics graphics, String text, int x, int y, boolean dropShadow) {
        this.write(mc, graphics, Component.literal(text), x, y, dropShadow);
    }

    public void write(Minecraft mc, GuiGraphics graphics, Component text, int x, int y, boolean dropShadow) {
        graphics.drawString(mc.font, text, x, y, CommonColors.WHITE, dropShadow);
    }

}
