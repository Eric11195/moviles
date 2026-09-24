package com.example.engine;

public class Engine implements Runnable{
    private Thread gameThread;

    private final GraphicsInterface graphics;

    private volatile boolean running;
    private Scene currentScene;

    public Engine(GraphicsInterface graphics) {
        this.graphics = graphics;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setScene(Scene scn){
        this.currentScene = scn;
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

    //Called when the application starts running, or is started on after switching from another screen
    public void resume() {
        if(running) return;

        running = true;
        gameThread = new Thread(this, "GameLoopThread");
        gameThread.start();
    }
    //Called when the application is paused (switched to another screen etc)
    public void pause() {
        if(!running) return;

        this.running = false;
        while (true) {
            try {
                this.gameThread.join();
                this.gameThread = null;
                break;
            } catch (InterruptedException ie) {
                // Esto no debería ocurrir nunca...
                Thread.currentThread().interrupt();
            }
        }
    }

    //This function will be called when starting the thread via Engine::resume
    public void run(){
        if (gameThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }
        while(this.running && !this.correctlyResumedBoolean());

        this.start();
        while(this.running){
            this.update();
        }
    }
    /** Returns true if everything was correctly setup after calling resume
     * The moment this returns true the game loop will be active
     */
    protected boolean correctlyResumedBoolean(){
        return true;
    }

    private double dt;
    //time since start
    private double t;
    private long lastFrameTime;
    long prevTime = 0;
    int frames = 0;
    protected void start(){
        lastFrameTime = System.nanoTime();

        prevTime = lastFrameTime; // Informes de FPS
        frames = 0;
        dt = t = 0;
    }

    protected void update(){
        long currentTime = System.nanoTime();
        long nanoElapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        currentScene.update(this,dt);
        this.render();
        // Informe de FPS
        dt = (double) nanoElapsedTime / 1.0E9;
        t = (double) currentTime / 1.0E9;
        if (currentTime - prevTime > 1000000000l) {
            long fps = frames * 1000000000l / (currentTime - prevTime);
            System.out.println("" + fps + " fps");
            frames = 0;
            prevTime = currentTime;
        }
        ++frames;
    }
    protected void render(){
        graphics.render(this,dt);
    }
}