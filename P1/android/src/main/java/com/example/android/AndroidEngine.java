package com.example.android;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

import java.util.function.Consumer;

public class AndroidEngine {

    private final Engine engine;
    private final GraphicsAndroid graphics;

    private Thread gameThread;

    public AndroidEngine(
            AppCompatActivity activity,
            int surfaceViewId,
            Consumer<Engine> gameStartFunc) {

        graphics = new GraphicsAndroid(activity);
        graphics.init(surfaceViewId);

        engine = new Engine((GraphicsInterface) graphics);

        SurfaceView surface =
                activity.findViewById(surfaceViewId);

        SurfaceHolder holder =
                surface.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startGame(gameStartFunc);
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
                stopGame();
            }
        });
    }

    private void startGame(Consumer<Engine> gameStartFunc) {

        if (gameStartFunc == null) {
            Log.e("ENGINE", "No app start callback!");
            return;
        }

        engine.start();

        gameThread = new Thread(() -> {
            gameStartFunc.accept(engine);
        });

        gameThread.start();
    }

    private void stopGame() {

        engine.stop();

        if (gameThread != null &&
                Thread.currentThread() != gameThread) {

            try {
                gameThread.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Engine getEngine() {
        return engine;
    }
}