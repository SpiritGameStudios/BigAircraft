package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Order the texture file as follows:
 * <pre>0123456789-.</pre>
 */
public enum NumericalFont {

    BIG_BUBBLE("gui/sprites/aviation_display/font_big_bubble", -1, 8, 4, 12, 96),
    STOCK_SANS("gui/sprites/aviation_display/font_stock_sans", 1, 3, 1, 5, 36);

    public final ResourceLocation id;
    public final int padding;
    public final int characterWidth;
    public final int pointCharWidth;
    public final int textureHeight;
    public final int textureWidth;

    /**
     * @param path The file path for this font inside the textures/ directory.
     * @param padding The space that should be applied between each character during rendering. Use negative numbers for textures with outlines.
     * @param charWidth The width of each character, in pixels.
     * @param pointCharWidth The width of the decimal point "<code>.</code>" character, in pixels.
     * @param textureHeight The total height of the texture file, in pixels.
     * @param textureWidth The total width of the texture file, in pixels.
     */
    NumericalFont(String path, int padding, int charWidth, int pointCharWidth, int textureHeight, int textureWidth) {
        this.id = BigAircraft.id("textures/" + path + ".png");
        this.padding = padding;
        this.characterWidth = charWidth;
        this.pointCharWidth = pointCharWidth;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
    }

}