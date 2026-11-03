package com.example.androidengine;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.engine.FontEng;

//To load a font put it inside res/font
public class FontAndroid implements FontEng {
    private float fontSize;
    private final Typeface tf;
    //Do not mark bold if yout font.ttf is bold by default
    public FontAndroid(Context context, String fontName, float fontSize, boolean bold){
        // Load the base font directly from the assets folder
        Typeface baseTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        // Apply bold styling if needed
        this.tf = Typeface.create(
                baseTypeface,
                bold ? Typeface.BOLD : Typeface.NORMAL
        );
        this.fontSize = fontSize;
    }
    //Creates a font with the default font resource
    //Do not mark bold if yout font.ttf is bolf by default
    public FontAndroid(float fontSize, boolean bold){
        this.tf = bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
        this.fontSize = fontSize;
    }
    //Creates a default font with size 16
    public FontAndroid(){
        this.tf = Typeface.DEFAULT;
        this.fontSize = 16;
    }
    public final Typeface getTypeface(){
        return tf;
    }
    public final float getFontSize(){
        return fontSize;
    }
    @Override
    public void setFontSize(float newSize){
        this.fontSize = newSize;
    }
}
