package com.example.androidengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.EngImage;

//To load an image put it inside res/drawable
public class ImageAndroid implements EngImage {
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
    @Override
    public final int getWidth(){
        return bm.getWidth();
    }
    @Override
    public final int getHeight(){
        return bm.getHeight();
    }
}
