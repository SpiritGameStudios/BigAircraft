package dev.spiritstudios.aerobig.client.render;

public enum Alignment {

    LEFT, RIGHT, CENTER;

    public int getOffset(int width) {
        return switch (this) {
            case LEFT -> 0;
            case RIGHT -> width;
            case CENTER -> width / 2;
        };
    }
}