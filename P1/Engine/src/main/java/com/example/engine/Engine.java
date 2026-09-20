package com.example.engine;

public class Engine {

    private final GraphicsInterface graphics;

    private volatile boolean running;

    public Engine(GraphicsInterface graphics) {
        this.graphics = graphics;
    }

    public GraphicsInterface getGraphics() {
        return graphics;
    }

    public boolean getRunning() {
        return running;
    }

    public void endEngine() {
        running = false;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }
}