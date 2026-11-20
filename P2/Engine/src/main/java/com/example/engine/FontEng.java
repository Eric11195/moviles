package com.example.engine;

/**
 * Abstract platform-independent representation of a font.
 */
public abstract class FontEng {

    /** Font asset file name. */
    public final String fileName;

    /** Size of the font in points. */
    public int fontSize;

    /**
     * Constructs a FontEng instance with specified font file name and size.
     *
     * @param fileName font file name
     * @param fontSize font size in points
     */
    public FontEng(String fileName, int fontSize) {
        this.fileName = fileName;
        this.fontSize = fontSize;
    }

    /**
     * Sets the font size.
     *
     * @param new_size new font size in points
     */
    protected void setFontSize(int new_size) {
        this.fontSize = new_size;
    }
}
