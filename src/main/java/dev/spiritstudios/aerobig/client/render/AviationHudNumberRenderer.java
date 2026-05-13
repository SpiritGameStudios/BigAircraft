package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class AviationHudNumberRenderer {

    private static final int PADDING = 1;
    private static final int MINUS_SIGN_INDEX = 10;
    private static final int DECIMAL_POINT_INDEX = 11;

    private final GuiGraphics graphics;
    private final Font font;

    private int cursor;

    public AviationHudNumberRenderer(GuiGraphics graphics, Font font) {
        this.graphics = graphics;
        this.font = font;
    }

    public void drawInt(int number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        if (number < 0)
            this.renderAndMoveCursor(MINUS_SIGN_INDEX, this.font.characterWidth, y);

        this.renderChars(Integer.toString(Math.abs(number)), y);
    }

    public void drawDouble(double number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        if (number < 0)
            this.renderAndMoveCursor(MINUS_SIGN_INDEX, this.font.characterWidth, y);

        String[] parts = "%.2f".formatted(Math.abs(number)).split("\\.");

        this.renderChars(parts[0], y);
        this.renderAndMoveCursor(DECIMAL_POINT_INDEX, this.font.pointCharWidth, y);
        this.renderChars(parts[1], y);
    }

    private int getWidth(int number) {
        return Integer.toString(number).length() * (this.font.characterWidth + PADDING) - PADDING;
    }

    private int getWidth(double number) {
        String s = "%.2f".formatted(number).replace('.', '\0');
        return s.length() * (this.font.characterWidth + PADDING) + this.font.pointCharWidth;
    }

    private void renderChars(String number, int y) {
        for (char c : number.toCharArray())
            this.renderAndMoveCursor(c - '0', this.font.characterWidth, y);
    }

    private void renderAndMoveCursor(int index, int shift, int y) {
        int x1 = this.cursor;
        int x2 = x1 + this.font.characterWidth;

        int y1 = y + this.font.atlasHeight / 2;
        int y2 = y1 + font.atlasHeight;


        float u1 = (this.font.characterWidth * index + 0.0F) / (float) this.font.atlasWidth;
        float u2 = ((this.font.characterWidth * index) + (float) this.font.characterWidth) / (float) this.font.atlasWidth;
        float v1 = 0.0f;
        float v2 = 1.0f;

        Matrix4f pose = graphics.pose().last().pose();
        this.graphics.bufferSource().getBuffer(BigAircraftRenderTypes.NUMBER_INVERT.apply(font.id))
                .addVertex(pose, x1, y1, 0).setUv(u1, v1)
                .addVertex(pose, x1, y2, 0).setUv(u1, v2)
                .addVertex(pose, x2, y2, 0).setUv(u2, v2)
                .addVertex(pose, x2, y1, 0).setUv(u2, v1);

        this.cursor += shift + PADDING;
    }

    public enum Font {
        BOLD("gui/sprites/aviation_display/numerical_bold", 6, 2, 10, 72),
        SMALL("gui/sprites/aviation_display/numerical_small", 3, 1, 5, 36);

        private final ResourceLocation id;
        private final int characterWidth;
        private final int pointCharWidth;
        private final int atlasHeight;
        private final int atlasWidth;

        Font(String path, int characterWidth, int pointCharWidth, int atlasHeight, int atlasWidth) {
            this.id = BigAircraft.id("textures/" + path + ".png");
            this.characterWidth = characterWidth;
            this.pointCharWidth = pointCharWidth;
            this.atlasHeight = atlasHeight;
            this.atlasWidth = atlasWidth;
        }
    }

    public enum Alignment {
        LEFT,
        RIGHT,
        CENTER;

        public int getOffset(int width) {
            return switch (this) {
                case LEFT -> 0;
                case RIGHT -> width;
                case CENTER -> width / 2;
            };
        }
    }
}
