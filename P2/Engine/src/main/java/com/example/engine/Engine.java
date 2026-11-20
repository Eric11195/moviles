package com.example.engine;

import com.example.engine.audio.AudioInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Core abstract game engine managing the main game loop, scene stack transitions, subsystems (graphics, audio, input, file storage, ads, notifications, social), and timing.
 */
public abstract class Engine implements Runnable {

    /** Main game execution thread. */
    private Thread gameThread;

    /** Subsystem for graphics and rendering. */
    private final GraphicsInterface graphics;

    /** Subsystem for input handling. */
    private final InputBase input;

    /** Subsystem for audio processing. */
    private final AudioInterface audio;

    /** Subsystem for file storage. */
    private final IFileStorage fileStorage;

    /** Flag indicating whether the game loop is running. */
    private volatile boolean running;

    /** Currently active scene. */
    private Scene currentScene;

    /**
     * Inner class representing a queued scene management operation to be deferred to the end of a frame.
     */
    private static class sceneOperation {
        /**
         * Constructs a scene operation.
         *
         * @param o operation type
         * @param s scene instance
         */
        sceneOperation(op o, Scene s) {
            myOp = o;
            scn = s;
        }

        /** Operation type. */
        op myOp;

        /** Scene instance associated with the operation. */
        Scene scn;
    }

    /**
     * Enum specifying the types of scene stack operations.
     */
    private static enum op {
        /** Push scene onto stack. */
        push,
        /** Pop top scene off stack. */
        pop,
        /** Replace current stack with new scene. */
        set
    }

    /** Active scene stack. */
    ArrayList<Scene> sceneStack = new ArrayList<Scene>();

    /** Pending scene operations queue. */
    ArrayList<sceneOperation> sceneOpQueue = new ArrayList<sceneOperation>();

    /** Subsystem for advertising. */
    private final AdsInterface ads;

    /** Subsystem for notifications. */
    private final NotificationInterface notifs;

    /** Subsystem for social integration. */
    private final ISocial social;

    /**
     * Returns the social subsystem.
     *
     * @return ISocial instance
     */
    public ISocial getSocial() { return social; }

    /**
     * Constructs the core Engine with all platform subsystems.
     *
     * @param graphics graphics subsystem implementation
     * @param input input subsystem implementation
     * @param audio audio subsystem implementation
     * @param fileStorage file storage subsystem implementation
     * @param ads ads subsystem implementation
     * @param notifs notifications subsystem implementation
     * @param social social subsystem implementation
     */
    protected Engine(GraphicsInterface graphics, InputBase input, AudioInterface audio, IFileStorage fileStorage, AdsInterface ads, NotificationInterface notifs, ISocial social) {
        this.graphics = graphics;
        this.input = input;
        this.audio = audio;
        this.fileStorage = fileStorage;
        this.ads = ads;
        this.ads.initialize();
        this.notifs = notifs;
        this.social = social;
    }

    /**
     * Retrieves the currently active scene.
     *
     * @return current active Scene instance
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Changes current scene stack to scn, clearing previous scenes.
     *
     * @param scn new scene to set as active
     */
    public void setScene(Scene scn) {
        assert(scn != null);
        sceneOpQueue.add(new sceneOperation(op.set, scn));
    }

    /**
     * Pushes scn onto the top of the scene stack without destroying the previous scene.
     *
     * @param scn new scene to push
     */
    public void pushScene(Scene scn) {
        assert(scn != null);
        sceneOpQueue.add(new sceneOperation(op.push, scn));
    }

    /**
     * Removes the top scene from the stack, restoring the previous scene as active.
     */
    public void popScene() {
        sceneOpQueue.add(new sceneOperation(op.pop, null));
    }

    /**
     * Returns the graphics subsystem.
     *
     * @return GraphicsInterface instance
     */
    public GraphicsInterface getGraphics() { return graphics; }

    /**
     * Returns the input subsystem.
     *
     * @return InputBase instance
     */
    public InputBase getInput() { return input; }

    /**
     * Returns the audio subsystem.
     *
     * @return AudioInterface instance
     */
    public AudioInterface getAudio() { return audio; }

    /**
     * Returns the file storage subsystem.
     *
     * @return IFileStorage instance
     */
    public IFileStorage getFileStorage() { return fileStorage; }

    /**
     * Returns the ads subsystem.
     *
     * @return AdsInterface instance
     */
    public AdsInterface getAds() { return this.ads; }

    /**
     * Returns the notification subsystem.
     *
     * @return NotificationInterface instance
     */
    public NotificationInterface getNotifs() { return notifs; }

    /**
     * Checks if the engine loop is running.
     *
     * @return true if running, false otherwise
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * Stops the main engine loop.
     */
    public void endEngine() {
        running = false;
    }

    /**
     * Starts or resumes the game thread.
     */
    public void resume() {
        if (running) return;

        running = true;
        gameThread = new Thread(this, "GameLoopThread");
        gameThread.start();
    }

    /**
     * Pauses the game thread and waits for it to join.
     */
    public void pause() {
        if (!running) return;
        this.running = false;
        while (true) {
            try {
                this.gameThread.join();
                this.gameThread = null;
                break;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Main thread run loop executing the game update loop.
     */
    @Override
    public void run() {
        if (gameThread != Thread.currentThread()) {
            throw new RuntimeException("run() should not be called directly");
        }
        while (this.running && !this.correctlyResumedBoolean());
        this.start();
        while (this.running) {
            this.update();
        }
    }

    /**
     * Returns true if everything was correctly set up after calling resume.
     *
     * @return true if ready to activate game loop, false otherwise
     */
    protected boolean correctlyResumedBoolean() {
        return this.ads.isReady();
    }

    /** Delta time for the current frame in seconds. */
    private float dt = 0;

    /**
     * Returns delta time for the current frame.
     *
     * @return delta time in seconds
     */
    public float getDt() { return dt; }

    /** Total elapsed time since engine start in seconds. */
    private float t = 0;

    /**
     * Returns total elapsed time since engine start.
     *
     * @return elapsed time in seconds
     */
    public float getTime() { return t; }

    /** Timestamp of the last frame in nanoseconds. */
    private long lastFrameTime;

    /** Timestamp of the previous FPS reporting interval. */
    long prevTime = 0;

    /** Frame count counter for FPS calculations. */
    int frames = 0;

    /**
     * Initializes engine timing variables and start logic.
     */
    protected void start() {
        lastFrameTime = System.nanoTime();

        prevTime = lastFrameTime;
        frames = 0;
        dt = t = 0;

        System.out.println("Engine: Calling ads.initialize()");
    }

    /**
     * Processes pending scene operations (push, pop, set) queued during the frame.
     */
    private void manageSceneOperations() {
        List<sceneOperation> operationsToProcess = new ArrayList<>(sceneOpQueue);
        sceneOpQueue.clear();
        for (sceneOperation op : operationsToProcess) {
            switch (op.myOp) {
                case pop: {
                    Scene poppedScene = sceneStack.get(sceneStack.size() - 1);
                    poppedScene.pause(this);
                    poppedScene.cleanup(this);
                    sceneStack.remove(sceneStack.size() - 1);
                    if (!sceneStack.isEmpty()) {
                        Scene newCurrentScene = sceneStack.get(sceneStack.size() - 1);
                        newCurrentScene.resume(this);
                    }
                }
                break;
                case push: {
                    if (!sceneStack.isEmpty()) {
                        Scene previousScene = sceneStack.get(sceneStack.size() - 1);
                        previousScene.pause(this);
                    }
                    sceneStack.add(op.scn);
                    Scene newScene = sceneStack.get(sceneStack.size() - 1);
                    newScene.start(this);
                    newScene.resume(this);
                }
                break;
                case set: {
                    for (int i = sceneStack.size() - 1; i >= 0; --i) {
                        Scene scn = sceneStack.get(sceneStack.size() - 1);
                        scn.pause(this);
                        scn.cleanup(this);
                    }
                    sceneStack.clear();
                    sceneStack.add(op.scn);
                    Scene newScene = sceneStack.get(sceneStack.size() - 1);
                    newScene.start(this);
                    newScene.resume(this);
                }
                break;
                default:
                    throw new RuntimeException("Non valid scene operation used");
            }
        }
        if (sceneStack.isEmpty()) {
            throw new RuntimeException("Scene stack cannot be empty before calling update. You must use pushScene, or setScene");
        }
        currentScene = sceneStack.get(sceneStack.size() - 1);
    }

    /**
     * Single step update of the game loop: processes timing, scene operations, input, and scene update.
     */
    protected void update() {
        long currentTime = System.nanoTime();
        long nanoElapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        manageSceneOperations();

        currentScene.update(this, t, dt);
        this.render(this.getGraphics());
        input.update(this.graphics::getPointInWindowPos);

        dt = (float) (nanoElapsedTime / 1.0E9);
        t = (float) (currentTime / 1.0E9);
        if (currentTime - prevTime > 1000000000L) {
            long fps = frames * 1000000000L / (currentTime - prevTime);
            System.out.println("" + fps + " fps");
            frames = 0;
            prevTime = currentTime;
        }
        ++frames;
    }

    /**
     * Renders the scene to screen using the graphics subsystem.
     *
     * @param g GraphicsInterface instance
     */
    protected abstract void render(GraphicsInterface g);
}
