package com.example.androidengine;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidengine.audio.AudioAndroid;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

/**
 * Android platform engine implementation bridging SurfaceView events to the core Engine lifecycle.
 */
public class AndroidEngine extends Engine {

    /** Flag indicating whether the rendering SurfaceView holder is created and active. */
    boolean readyToStart = false;

    /**
     * Constructs AndroidEngine with Activity context and layout view resource IDs.
     *
     * @param activity hosting AppCompatActivity
     * @param surfaceViewId SurfaceView resource ID
     * @param addView AdView resource ID
     * @param width logical width
     * @param height logical height
     */
    public AndroidEngine(
            AppCompatActivity activity,
            int surfaceViewId, int addView, int width, int height) {
        super(
            new GraphicsAndroid(activity, surfaceViewId, addView, width, height),
            new InputAndroid(),
            new AudioAndroid(activity),
            new FileStorageAndroid(activity),
            new AdsAndroid(activity, addView),
            new NotificationAndroid(activity),
            new SocialAndroid(activity)
        );

        SurfaceView surface = activity.findViewById(surfaceViewId);
        SurfaceHolder holder = surface.getHolder();

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
        ((InputAndroid) getInput()).registerView(surface);
    }

    /**
     * Checks if surface is created and ready for rendering.
     *
     * @return true if surface is active, false otherwise
     */
    @Override
    protected boolean correctlyResumedBoolean() {
        return readyToStart;
    }

    /**
     * Renders current scene using GraphicsAndroid frame lifecycle.
     *
     * @param g active GraphicsInterface context
     */
    @Override
    protected void render(GraphicsInterface g) {
        if (g.prepareFrame()) {
            this.getCurrentScene().render(g);
            g.endFrame();
        }
    }
}
