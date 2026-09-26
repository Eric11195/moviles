package com.example.androidengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.ImageEng;
import com.example.utils.Utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

//To load an image put it inside res/drawable
public class ImageAndroid implements ImageEng {
    private Bitmap bm;
    // Id looks like R.drawable.imageName on res/drawable
    public ImageAndroid(String path){
        bm = BitmapFactory.decodeFile(
                path
        );
        String current_path = Utils.getCurrentPath();
        if(bm == null){
            Log.d("There's no valid image in path: ",current_path+" : "+path);
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
