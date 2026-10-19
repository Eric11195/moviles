package com.example.androidengine;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Consumer;

import com.example.androidengine.audio.AudioAndroid;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

public class AndroidEngine extends Engine{
    boolean readyToStart = false;
    public AndroidEngine(
            AppCompatActivity activity,
            int surfaceViewId, int width, int height) {
        super(new GraphicsAndroid(activity, surfaceViewId, width, height),new InputAndroid(),new AudioAndroid(activity), new FileStorageAndroid(activity));

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
        ((InputAndroid)getInput()).registerView(surface);
    }
    @Override
    protected boolean correctlyResumedBoolean(){
        return readyToStart;
    }
    @Override
    protected void render(GraphicsInterface g){
        g.prepareFrame();
        this.getCurrentScene().render(g);
        g.endFrame();
    }
}