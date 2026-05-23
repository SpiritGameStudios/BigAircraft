package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.function.Function;

public class FlightHudNumberRenderer {

    private static final int MINUS_SIGN_INDEX = 10;
    private static final int DECIMAL_POINT_INDEX = 11;

    private final GuiGraphics graphics;
    private final MonoNumberFont font;
    private final Function<ResourceLocation, RenderType> renderType;

    private Alignment alignment = Alignment.LEFT;
    private int decimalPlaces = 2;
    private int paddedZeroPlaces = 1;

    private int cursor;

    public FlightHudNumberRenderer(GuiGraphics graphics, MonoNumberFont font, Function<ResourceLocation, RenderType> renderType) {
        this.graphics = graphics;
        this.font = font;
        this.renderType = renderType;
    }

    public void alignTo(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setPaddedZeroPlaces(int paddedZeroPlaces) {
        this.paddedZeroPlaces = paddedZeroPlaces;
    }

    private String formatInt(int number) {
        return String.format(Locale.US, "%0" + this.paddedZeroPlaces + "d", number);
    }

    private String formatDouble(double number) {
        return String.format(Locale.US, "%0" + this.paddedZeroPlaces + "." + this.decimalPlaces + "f", number);
    }

    public void drawInt(int number, int x, int y) {
        String s = this.formatInt(number);
        this.cursor = x - this.alignment.getOffset(s.length() * this.xShift() - this.font.spacing());

        this.renderChars(this.formatInt(number), y);
    }

    public void drawDouble(double number, int x, int y) {
        String s = this.formatDouble(number);
        this.cursor = x - this.alignment.getOffset(s.replace(".", "").length() * this.xShift() + this.font.pointCharWidth());

        this.renderChars(s, y);
    }

    private int xShift() {
        return this.font.charWidth() + this.font.spacing();
    }

    private void renderChars(String number, int y) {
        for (char c : number.toCharArray()) {
            if (c >= '0' && c <= '9')
                this.renderAndMoveCursor(c - '0', this.xShift(), y);
            else if (c == '.')
                this.renderAndMoveCursor(DECIMAL_POINT_INDEX, this.font.pointCharWidth() + this.font.spacing(), y);
            else {
                if (c != '-')
                    BigAircraft.LOGGER.warn("Cannot render character '{}' with FlightHudNumberRenderer. Using '-' instead.", c);

                this.renderAndMoveCursor(MINUS_SIGN_INDEX, this.xShift(), y);
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

        this.cursor += shift;
    }

}
