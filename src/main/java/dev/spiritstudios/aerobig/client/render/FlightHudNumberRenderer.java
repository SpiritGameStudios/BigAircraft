package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class FlightHudNumberRenderer {

    private static final int MINUS_SIGN_INDEX = 10;
    private static final int DECIMAL_POINT_INDEX = 11;

    private final GuiGraphics graphics;
    private final NumericalFont font;
    private final Function<ResourceLocation, RenderType> renderType;

    private int cursor;

    public FlightHudNumberRenderer(GuiGraphics graphics, NumericalFont font, Function<ResourceLocation, RenderType> renderType) {
        this.graphics = graphics;
        this.font = font;
        this.renderType = renderType;
    }

    public NumericalFont getFont() {
        return this.font;
    }

    public void drawInt(int number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        this.renderChars(Integer.toString(number), y);
    }

    public void drawDouble(double number, int x, int y, Alignment alignment) {
        this.cursor = x - alignment.getOffset(this.getWidth(number));

        this.renderChars("%01.2f".formatted(number), y);
    }

    public int getWidth(int number) {
        return Integer.toString(number).length() * (this.font.charWidth() + this.font.spacing()) - this.font.spacing();
    }

    public int getWidth(double number) {
        String s = "%.2f".formatted(number).replace(".", "");
        return s.length() * (this.font.charWidth() + this.font.spacing()) + this.font.pointCharWidth();
    }

    private void renderChars(String number, int y) {
        for (char c : number.toCharArray()) {
            if (Character.isDigit(c)) {
                this.renderAndMoveCursor(c - '0', this.font.charWidth(), y);
            } else if (c == '.' || c == ',') { // TODO: comma in spritesheet
                this.renderAndMoveCursor(DECIMAL_POINT_INDEX, this.font.pointCharWidth(), y);
            } else if (c == '-') {
                this.renderAndMoveCursor(MINUS_SIGN_INDEX, this.font.charWidth(), y);
            } else {
                BigAircraft.LOGGER.warn("Cannot render character '{}' with FlightHudNumberRenderer", c);
            }
        }
    }

    private void renderAndMoveCursor(int index, int shift, int y) {
        FlightHudRenderer.renderSprite(
                this.graphics,
                this.font.id(),
                this.cursor,
                y,
                this.font.charWidth(),
                this.font.textureHeight(),
                this.font.charWidth() * index,
                0.0F,
                this.font.textureWidth(),
                this.font.textureHeight(),
                this.renderType
        );

        this.cursor += shift + this.font.spacing();
    }

}
