package com.example.engine;

import java.awt.Color;

public class ColorEng {
    public int r,g,b,a;
    public ColorEng(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    //This consumes argb format
    public ColorEng(int color){
        this.r = (color >> 16) & 0xff;
        this.g = (color >> 8) & 0xff;
        this.b = color & 0xff;
        this.a = (color >> 24) & 0xff;
    }
    public int getColorAsInt(){
        // Variables a, r, g, b should be integers between 0 and 255
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }
    public ColorEng clone() {
        return new ColorEng(r,g,b,a);
    }
}
