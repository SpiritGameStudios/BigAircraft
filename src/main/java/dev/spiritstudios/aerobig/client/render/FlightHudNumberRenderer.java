package dev.spiritstudios.aerobig.client.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class FlightHudNumberRenderer {

    private static final int PADDING = 1;
    private static final int MINUS_SIGN_INDEX = 10;
    private static final int DECIMAL_POINT_INDEX = 11;

    private final GuiGraphics graphics;
    public final NumericalFont font;
    private final Function<ResourceLocation, RenderType> renderType;

    private int cursor;

    public FlightHudNumberRenderer(GuiGraphics graphics, NumericalFont font, Function<ResourceLocation, RenderType> renderType) {
        this.graphics = graphics;
        this.font = font;
        this.renderType = renderType;
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
        String s = "%.2f".formatted(number).replace(".", "");
        return s.length() * (this.font.characterWidth + PADDING) + this.font.pointCharWidth;
    }

    private void renderChars(String number, int y) {
        for (char c : number.toCharArray())
            this.renderAndMoveCursor(c - '0', this.font.characterWidth, y);
    }

    private void renderAndMoveCursor(int index, int shift, int y) {
        FlightHudRenderer.renderSprite(
            this.graphics,
            this.font.id,
            this.cursor,
            y,
            this.font.characterWidth,
            this.font.atlasHeight,
            this.font.characterWidth * index,
            0.0F,
            this.font.atlasWidth,
            this.font.atlasHeight,
            this.renderType
        );

        this.cursor += shift;
        this.cursor += this.font.outlineWidth.isPresent() ? -this.font.outlineWidth.getAsInt() : PADDING;
    }

}
