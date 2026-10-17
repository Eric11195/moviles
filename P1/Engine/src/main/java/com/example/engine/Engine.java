package com.example.engine;
import com.example.engine.audio.AudioInterface;

import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Engine implements Runnable{
    private Thread gameThread;

    private final GraphicsInterface graphics;

    private final InputBase input;

    private final AudioInterface audio;
    private final IFileStorage fileStorage;

    private volatile boolean running;
    private Scene currentScene;
    //This class allows to delay the operations on scene managemente until end of frame
    private static class sceneOperation{
        sceneOperation(op o, Scene s){
            myOp = o;
            scn = s;
        }
        op myOp;
        Scene scn;
    };
    private static enum op{
        push,
        pop,
        set
    }

    ArrayList<Scene> sceneStack = new ArrayList<Scene>();
    ArrayList<sceneOperation> sceneOpQueue=new ArrayList<sceneOperation>();

    protected Engine(GraphicsInterface graphics, InputBase input, AudioInterface audio, IFileStorage fileStorage) {
        this.graphics = graphics;
        this.input = input;
        this.audio = audio;
        this.fileStorage = fileStorage;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Changes currents scene to scn.
     * Start will be called on the new scene
     * @param scn
     */
    public void setScene(Scene scn){
        assert(scn != null);
        sceneOpQueue.add(new sceneOperation(op.set,scn));
    }

    /**
     * Changes current scene to scn. Does not eliminate the previous scene
     * @param scn
     */
    public void pushScene(Scene scn){
        assert(scn!=null);
        sceneOpQueue.add(new sceneOperation(op.push, scn));
    }

    /**
     * Removes current scene from the queue. If there was a previous one that will be set as active now
     * @param
     */
    public void popScene(){
        sceneOpQueue.add(new sceneOperation(op.pop, null));
    }

    public GraphicsInterface getGraphics() {return graphics;}

    public InputBase getInput() {return input; }
    public AudioInterface getAudio() {return audio; }
    public IFileStorage getFileStorage(){return fileStorage; }

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

    private float dt=0;
    public float getDt(){return dt;}
    //time since start
    private float t=0;
    public float getTime(){return t;}
    private long lastFrameTime;
    long prevTime = 0;
    int frames = 0;
    protected void start(){
        lastFrameTime = System.nanoTime();

        prevTime = lastFrameTime; // Informes de FPS
        frames = 0;
        dt = t = 0;
    }

    private void manageSceneOperations(){
        List<sceneOperation> operationsToProcess = new ArrayList<>(sceneOpQueue);
        sceneOpQueue.clear();
        for(sceneOperation op : operationsToProcess){
            switch(op.myOp){
                case pop: {
                    Scene poppedScene = sceneStack.get(sceneStack.size() - 1);
                    poppedScene.pause(this);
                    poppedScene.cleanup(this);
                    sceneStack.remove(sceneStack.size() - 1);
                    if (!sceneOpQueue.isEmpty()) {
                        Scene newCurrentScene = sceneStack.get(sceneStack.size()-1);
                        newCurrentScene.resume(this);
                    }
                }
                break;
                case push:{
                    if(!sceneStack.isEmpty()) {
                        Scene previousScene = sceneStack.get(sceneStack.size() - 1);
                        previousScene.pause(this);
                    }
                    sceneStack.add(op.scn);
                    Scene newScene = sceneStack.get(sceneStack.size()-1);
                    newScene.start(this);
                    newScene.resume(this);
                }
                break;
                case set:{
                    for(int i = sceneStack.size()-1; i>=0; --i){
                        Scene scn = sceneStack.get(sceneStack.size()-1);
                        scn.pause(this);
                        scn.cleanup(this);
                    }
                    sceneStack.clear();
                    sceneStack.add(op.scn);
                    Scene newScene = sceneStack.get(sceneStack.size()-1);
                    newScene.start(this);
                    newScene.resume(this);
                }
                break;
                default:
                    throw new RuntimeException("Non valid scene operation used");
            }
        }
        if(sceneStack.isEmpty())
            throw new RuntimeException("Scene stack cannot be empty before calling update. You must use pushScene, or setScene");
        currentScene = sceneStack.get(sceneStack.size()-1);
    }

    protected void update(){
        long currentTime = System.nanoTime();
        long nanoElapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        manageSceneOperations();

        currentScene.update(this,t,dt);
        this.render(this.getGraphics());
        input.update(this.graphics::getPointInWindowPos);
        // Informe de FPS
        dt = (float)(nanoElapsedTime / 1.0E9);
        t =  (float)(currentTime / 1.0E9);
        if (currentTime - prevTime > 1000000000L) {
            long fps = frames * 1000000000L / (currentTime - prevTime);
            System.out.println("" + fps + " fps");
            frames = 0;
            prevTime = currentTime;
        }
        ++frames;
    }
    protected abstract void render(GraphicsInterface g);
}