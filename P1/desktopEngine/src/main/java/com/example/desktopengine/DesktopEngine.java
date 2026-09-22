package com.example.desktopengine;

import java.util.function.Consumer;
import com.example.engine.Engine;

public class DesktopEngine {
    private Engine engine;
    private GraphicsDesktop graphics;
    private Thread gameThread;
    public DesktopEngine(int width, int height, Consumer<Engine> startFunc){
        graphics = new GraphicsDesktop();

        graphics.init(0);

        engine = new Engine(graphics);

        startGame(startFunc);
    }
    public void startGame(Consumer<Engine> startFunc) {
        if (startFunc == null) {
            System.err.println("ENGINE: No app start callback!");
            return;
        }

        engine.start();

        gameThread = new Thread(() -> {
            startFunc.accept(engine);
        }, "GameLoopThread");

        gameThread.start();
    }

    public void stopGame() {
        engine.stop();

        if (gameThread != null && Thread.currentThread() != gameThread) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
