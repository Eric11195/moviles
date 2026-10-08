package com.example.engine;
import com.example.engine.audio.AudioInterface;

public class Engine implements Runnable{
    private Thread gameThread;

    private final GraphicsInterface graphics;

    private final InputBase input;

    private final AudioInterface audio;

    private volatile boolean running;
    private Scene currentScene;
    private Scene upcomingScene=null;

    protected Engine(GraphicsInterface graphics, InputBase input, AudioInterface audio) {
        this.graphics = graphics;
        this.input = input;
        this.audio = audio;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setScene(Scene scn){
        assert(scn != null);
        upcomingScene = scn;
    }

    public GraphicsInterface getGraphics() {return graphics;}

    public InputBase getInput() {return input;}

    public AudioInterface getAudio() {return audio;}

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

    private float dt;
    //time since start
    private float t;
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

        if(this.upcomingScene!=null){
            if(this.currentScene != null)
                this.currentScene.cleanup(this);
            this.currentScene = upcomingScene;
            this.currentScene.start(this);
        }

        currentScene.update(this,t,dt);
        this.render();
        input.update(this.graphics::getPointInWindowPos);
        // Informe de FPS
        dt = (float)(nanoElapsedTime / 1.0E9);
        t =  (float)(currentTime / 1.0E9);
        if (currentTime - prevTime > 1000000000l) {
            long fps = frames * 1000000000l / (currentTime - prevTime);
            //System.out.println("" + fps + " fps");
            frames = 0;
            prevTime = currentTime;
        }
        ++frames;
    }
    protected void render(){
        graphics.render(this);
    }
}