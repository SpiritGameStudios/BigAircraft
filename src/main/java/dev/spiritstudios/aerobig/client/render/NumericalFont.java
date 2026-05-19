package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Order the texture file as follows:
 * <code>0123456789-.</code>
 * @param id The resource location of the font.
 * @param spacing The space that should be applied between each character during rendering. Use negative numbers for textures with outlines.
 * @param charWidth The width of each character, in pixels.
 * @param pointCharWidth The width of the decimal point "<code>.</code>" character, in pixels.
 * @param textureWidth The total width of the texture file, in pixels.
 * @param textureHeight The total height of the texture file, in pixels.
 */
public record NumericalFont(ResourceLocation id, int spacing, int charWidth, int pointCharWidth, int textureWidth, int textureHeight) {

    public static final NumericalFont BIG_BLOCK = forOutline(BigAircraft.id("sprites/aviation_display/font_big_block"), 1, 8, 4, 96, 12);
    public static final NumericalFont STOCK_SANS = create(BigAircraft.id("sprites/aviation_display/font_stock_sans"), 3, 1, 36, 5);

    public static NumericalFont forOutline(ResourceLocation id, int outlineWidth, int charWidth, int pointCharWidth, int textureWidth, int textureHeight) {
        return create(id, -outlineWidth, charWidth, pointCharWidth, textureWidth, textureHeight);
    }

    public static NumericalFont create(ResourceLocation id, int charWidth, int pointCharWidth, int textureWidth, int textureHeight) {
        return create(id, 1, charWidth, pointCharWidth, textureWidth, textureHeight);
    }

    public static NumericalFont create(ResourceLocation id, int spacing, int charWidth, int pointCharWidth, int textureWidth, int textureHeight) {
        return new NumericalFont(id.withPrefix("textures/gui/").withSuffix(".png"), spacing, charWidth, pointCharWidth, textureWidth, textureHeight);
    }

}
