package com.example.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

//To load an image put it inside res/drawable
public class ImageAndroid {
    private Bitmap bm;
    // Id looks like R.drawable.imageName on res/drawable
    public ImageAndroid(AppCompatActivity act, int imageId) throws Exception{
        bm = BitmapFactory.decodeResource(
                act.getResources(),
                imageId
        );
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
