package com.example.androidengine;

import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

//To load a font put it inside res/font
public class FontAndroid {
    private float fontSize;
    private final Typeface tf;
    //Creates a font with the given font resource
    //Id looks like R.font.fontName on res/drawable
    //Do not mark bold if yout font.ttf is bold by default
    public FontAndroid(AppCompatActivity act, float fontSize, boolean bold, int fontResourceId){
        this.tf = Typeface.create(
                ResourcesCompat.getFont(act, fontResourceId),
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
    public void setFontSize(float newSize){
        this.fontSize = newSize;
    }
}
