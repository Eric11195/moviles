package com.example.engine;

/**
 * Platform-independent color representation with RGBA components.
 */
public class ColorEng {

    /** Red component (0-255). */
    public int r;

    /** Green component (0-255). */
    public int g;

    /** Blue component (0-255). */
    public int b;

    /** Alpha component (0-255). */
    public int a;

    /**
     * Constructs a ColorEng instance with explicit RGBA components.
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @param a alpha component (0-255)
     */
    public ColorEng(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Constructs a ColorEng instance from a packed 32-bit ARGB integer.
     *
     * @param color packed 32-bit ARGB color integer
     */
    public ColorEng(int color) {
        this.r = (color >> 16) & 0xff;
        this.g = (color >> 8) & 0xff;
        this.b = color & 0xff;
        this.a = (color >> 24) & 0xff;
    }

    /**
     * Packs RGBA components into a 32-bit ARGB integer.
     *
     * @return packed 32-bit ARGB color integer
     */
    public int getColorAsInt() {
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    /**
     * Creates a deep copy of this color instance.
     *
     * @return cloned ColorEng instance
     */
    public ColorEng clone() {
        return new ColorEng(r, g, b, a);
    }

    /**
     * Linearly interpolates between two colors.
     *
     * @param a start color
     * @param b end color
     * @param val interpolation factor [0.0 - 1.0]
     * @return new interpolated ColorEng instance
     */
    public static ColorEng interpolate(ColorEng a, ColorEng b, float val) {
        return new ColorEng(
            (int) (a.r * (1 - val) + b.r * val),
            (int) (a.g * (1 - val) + b.g * val),
            (int) (a.b * (1 - val) + b.b * val),
            (int) (a.a * (1 - val) + b.a * val)
        );
    }
}
