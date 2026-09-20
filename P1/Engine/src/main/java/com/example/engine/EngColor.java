package com.example.engine;

public class EngColor {
    int r,g,b,a;
    public EngColor(int r, int g, int b, int a){
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
