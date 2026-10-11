package com.example.engine;

public class ColorEng {
    public int r,g,b,a;
    public final static ColorEng RED = new ColorEng(255,0,0,255);
    public final static ColorEng GREEN = new ColorEng(0,255,0,255);
    public final static ColorEng BLUE = new ColorEng(0,0,255,255);
    public final static ColorEng WHITE = new ColorEng(255,255,255,255);
    public final static ColorEng BLACK = new ColorEng(0,0,0,255);
    public ColorEng(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public int getColorAsInt(){
        // Variables a, r, g, b should be integers between 0 and 255
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }
}
