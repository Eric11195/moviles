package com.example.androidengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.engine.ImageEng;
import com.example.utils.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

//To load an image put it inside res/drawable
public class ImageAndroid extends ImageEng {
    private Bitmap bm;
    // Id looks like R.drawable.imageName on res/drawable
    public ImageAndroid(Context context, String path){
        super(path);
        try (InputStream raw = context.getAssets().open(path);
             BufferedInputStream input = new BufferedInputStream(raw)) {

            bm = BitmapFactory.decodeStream(input);

            if (bm == null) {
                throw new RuntimeException("Failed to decode image: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load asset: " + path, e);
        }
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
