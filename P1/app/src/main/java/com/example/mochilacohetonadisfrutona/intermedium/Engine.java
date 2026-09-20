package com.example.mochilacohetonadisfrutona.intermedium;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Consumer;

public class Engine {
    public Engine(Consumer<Engine> gameStartfunc, AppCompatActivity activity, int surfaceViewId){
        androidActivity = activity;
        graphics = new GraphicsAndroid(activity);
        graphics.init(surfaceViewId);

        SurfaceView surface = activity.findViewById(surfaceViewId);
        android.view.SurfaceHolder holder = surface.getHolder();

        setAppStartCall(gameStartfunc);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                appStartCallback();
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
                Log.d("DRAW", "Surface destroyed!");
                stop();
            }
        });
    }
    public final boolean getRunning(){
        return running;
    }
    public void endEngine(){
        running = false;
    }
    public GraphicsInterface getGraphics(){
        assert(graphics!=null);
        return graphics;
    }


    //------------------------------------------------------------------------------------
    // PRIVATE METHODS & VARIABLES
    //------------------------------------------------------------------------------------
    private GraphicsInterface graphics = null;
    private AppCompatActivity androidActivity;

    private Consumer<Engine> executeOnStartFunc;
    private void stop() {
        //Sets running to false
        endEngine();

        if (gameThread != null && Thread.currentThread() != gameThread) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //This needs as a parameter a function that returns void and received engine as a parameter
    //This function will be called with the constructor of this class, when the game is ready to start
    private void setAppStartCall(Consumer<Engine> func){
        executeOnStartFunc = func;
    }
    private Thread gameThread;
    //mutex variable to only run one game loop at a time
    //If false the game should stop
    private volatile boolean running;
    private void appStartCallback(){
        //We use a thread in order not to block android main/UI thread
        if (executeOnStartFunc == null) {
            Log.e("ENGINE", "No app start callback!");
            return;
        }

        running = true;

        gameThread = new Thread(() -> {
            executeOnStartFunc.accept(this);
        });

        gameThread.start();
    }
}
