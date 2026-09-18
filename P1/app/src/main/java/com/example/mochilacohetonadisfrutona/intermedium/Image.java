package com.example.mochilacohetonadisfrutona.intermedium;

import android.graphics.Bitmap;

public class Image {
    private Bitmap bm;
    public Image() throws Exception{
        throw new Exception("Unimplemented");
    }
    public final Bitmap getBitmap(){
        return bm;
    }
    public final int getWidth(){
        return bm.getWidth();
    }
    public final int getHeight(){
        return bm.getHeight();
    }
}
