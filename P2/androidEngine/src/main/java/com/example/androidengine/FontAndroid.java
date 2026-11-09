package com.example.androidengine;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.engine.FontEng;

//To load a font put it inside res/font
public class FontAndroid extends FontEng {
    private final Typeface tf;
    //Do not mark bold if yout font.ttf is bold by default
    public FontAndroid(Context context, String fontName, int fontSize, boolean bold){
        super(fontName, fontSize);
        // Load the base font directly from the assets folder
        Typeface baseTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        // Apply bold styling if needed
        this.tf = Typeface.create(
                baseTypeface,
                bold ? Typeface.BOLD : Typeface.NORMAL
        );
    }
    //Creates a font with the default font resource
    //Do not mark bold if yout font.ttf is bolf by default
    public FontAndroid(int fontSize, boolean bold){
        super(null, fontSize);
        this.tf = bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
    }
    //Creates a default font with size 16
    public FontAndroid(){
        super(null, 16);
        this.tf = Typeface.DEFAULT;
    }
    public final Typeface getTypeface(){
        return tf;
    }
    public final float getFontSize(){
        return fontSize;
    }
    @Override
    public void setFontSize(int newSize){
        this.fontSize = newSize;
    }
}
