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
    //Creates a font with the given font resource
    //Id looks like R.font.fontName on res/drawable
    //Do not mark bold if yout font.ttf is bold by default
    public FontAndroid(Context context, String fontName, float fontSize, boolean bold){
        int resourceId = context.getResources().getIdentifier(
                fontName,
                "font",
                context.getPackageName()
        );
        this.tf = Typeface.create(
                ResourcesCompat.getFont(context, resourceId),
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
