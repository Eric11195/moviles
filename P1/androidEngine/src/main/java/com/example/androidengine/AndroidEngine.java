package com.example.androidengine;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Consumer;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

public class AndroidEngine extends Engine{
    boolean readyToStart = false;
    public AndroidEngine(
            AppCompatActivity activity,
            int surfaceViewId) {
        super(new GraphicsAndroid(activity, surfaceViewId));

        SurfaceView surface =
                activity.findViewById(surfaceViewId);

        SurfaceHolder holder =
                surface.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                readyToStart = true;
            }

            @Override
            public void surfaceChanged(
                    SurfaceHolder holder,
                    int format,
                    int width,
                    int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                readyToStart = false;
            }
        });
    }
    @Override
    protected boolean correctlyResumedBoolean(){
        return readyToStart;
    }
}