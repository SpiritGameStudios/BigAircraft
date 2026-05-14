package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.resources.ResourceLocation;

import java.util.OptionalInt;

public enum NumericalFont {
    BIG_BUBBLE("gui/sprites/aviation_display/font_big_bubble", OptionalInt.of(1), 8, 4, 12, 96),
    STOCK_SANS("gui/sprites/aviation_display/font_stock_sans", OptionalInt.empty(), 3, 1, 5, 36);

    public final ResourceLocation id;
    public final OptionalInt outlineWidth;
    public final int characterWidth;
    public final int pointCharWidth;
    public final int atlasHeight;
    public final int atlasWidth;

    NumericalFont(String path, OptionalInt outlineWidth, int characterWidth, int pointCharWidth, int atlasHeight, int atlasWidth) {
        this.id = BigAircraft.id("textures/" + path + ".png");
        this.outlineWidth = outlineWidth;
        this.characterWidth = characterWidth;
        this.pointCharWidth = pointCharWidth;
        this.atlasHeight = atlasHeight;
        this.atlasWidth = atlasWidth;
    }
}