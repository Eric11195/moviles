package com.example.mochilacohetonadisfrutona.intermedium;

import android.app.Activity;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

public class FontAndroid {
    private float fontSize;
    private final Typeface tf;
    //Creates a font with the given font resource
    public FontAndroid(Activity act, float fontSize, boolean bold, int fontResourceId){
        this.tf = Typeface.create(
                ResourcesCompat.getFont(act, fontResourceId),
                bold ? Typeface.BOLD : Typeface.NORMAL
        );
        this.fontSize = fontSize;
    }
    //Creates a font with the default font resource
    public FontAndroid(float fontSize, boolean bold){
        this.tf = bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
        this.fontSize = fontSize;
    }
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
