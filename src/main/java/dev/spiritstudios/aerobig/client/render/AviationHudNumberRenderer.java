package dev.spiritstudios.aerobig.client.render;

import com.mojang.blaze3d.vertex.*;
import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AviationHudNumberRenderer {

    private static final int PADDING = 1;
    private static final int MINUS_SIGN_INDEX = 10;
    private static final int DECIMAL_POINT_INDEX = 11;

    private final GuiGraphics graphics;
    private final Font font;
    private final RenderType renderType; // idk how to make this work :c help pls

    private int cursor;

    public AviationHudNumberRenderer(GuiGraphics graphics, Font font, RenderType renderType) {
        this.graphics = graphics;
        this.font = font;
        this.renderType = renderType;
    }

    public void drawInt(int number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        if (number < 0)
            this.blitAndMoveCursor(MINUS_SIGN_INDEX, this.font.characterWidth, y);

        this.blitChars(Integer.toString(Math.abs(number)), y);
    }

    public void drawDouble(double number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        if (number < 0)
            this.blitAndMoveCursor(MINUS_SIGN_INDEX, this.font.characterWidth, y);

        String[] parts = "%.2f".formatted(Math.abs(number)).split("\\.");

        this.blitChars(parts[0], y);
        this.blitAndMoveCursor(DECIMAL_POINT_INDEX, this.font.pointCharWidth, y);
        this.blitChars(parts[1], y);
    }

    private int getWidth(int number) {
        return Integer.toString(number).length() * (this.font.characterWidth + PADDING) - PADDING;
    }

    private int getWidth(double number) {
        String s = "%.2f".formatted(number).replace('.', '\0');
        return s.length() * (this.font.characterWidth + PADDING) + this.font.pointCharWidth;
    }

    private void blitChars(String number, int y) {
        for (char c : number.toCharArray())
            this.blitAndMoveCursor(c - '0', this.font.characterWidth, y);
    }

    private void blitAndMoveCursor(int index, int shift, int y) {
        this.graphics.blit(this.font.id, this.cursor, y + this.font.atlasHeight / 2, 0, this.font.characterWidth * index, 0, this.font.characterWidth, this.font.atlasHeight, this.font.atlasWidth, this.font.atlasHeight);
        this.cursor += shift + PADDING;
    }

    public enum Font {
        BOLD("gui/sprites/aviation_display/numerical_bold", 6, 2, 10, 72),
        SMALL("gui/sprites/aviation_display/numerical_small", 3, 1, 5, 36),
        ;

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
