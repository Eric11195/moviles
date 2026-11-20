package com.example.androidengine;

import android.content.Context;
import android.graphics.Typeface;

import com.example.engine.FontEng;

/**
 * Android implementation of {@link FontEng} encapsulating Android {@link Typeface}.
 */
public class FontAndroid extends FontEng {

    /** Android Typeface instance. */
    private final Typeface tf;

    /**
     * Constructs FontAndroid loading custom Typeface from assets folder.
     *
     * @param context Android Context
     * @param fontName font file name inside assets/fonts/
     * @param fontSize point size
     * @param bold true to apply bold style, false otherwise
     */
    public FontAndroid(Context context, String fontName, int fontSize, boolean bold) {
        super(fontName, fontSize);
        Typeface baseTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        this.tf = Typeface.create(
                baseTypeface,
                bold ? Typeface.BOLD : Typeface.NORMAL
        );
    }

    /**
     * Constructs FontAndroid using default system font.
     *
     * @param fontSize point size
     * @param bold true for bold system font, false otherwise
     */
    public FontAndroid(int fontSize, boolean bold) {
        super(null, fontSize);
        this.tf = bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
    }

    /**
     * Constructs default FontAndroid with size 16.
     */
    public FontAndroid() {
        super(null, 16);
        this.tf = Typeface.DEFAULT;
    }

    /**
     * Returns the underlying Android Typeface.
     *
     * @return Typeface instance
     */
    public final Typeface getTypeface() {
        return tf;
    }

    /**
     * Returns the font size.
     *
     * @return font size as float
     */
    public final float getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size.
     *
     * @param newSize new font size in points
     */
    @Override
    public void setFontSize(int newSize) {
        this.fontSize = newSize;
    }
}
