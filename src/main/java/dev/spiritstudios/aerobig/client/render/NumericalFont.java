package dev.spiritstudios.aerobig.client.render;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.resources.ResourceLocation;

public enum NumericalFont {
    BOLD("gui/sprites/aviation_display/numerical_bold", 6, 2, 10, 72),
    SMALL("gui/sprites/aviation_display/numerical_small", 3, 1, 5, 36);

    public final ResourceLocation id;
    public final int characterWidth;
    public final int pointCharWidth;
    public final int atlasHeight;
    public final int atlasWidth;

    NumericalFont(String path, int characterWidth, int pointCharWidth, int atlasHeight, int atlasWidth) {
        this.id = BigAircraft.id("textures/" + path + ".png");
        this.characterWidth = characterWidth;
        this.pointCharWidth = pointCharWidth;
        this.atlasHeight = atlasHeight;
        this.atlasWidth = atlasWidth;
    }
}