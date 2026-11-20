package com.example.engine;

/**
 * Interface defining lifecycle methods for game scenes (update, render, start, cleanup, resume, pause).
 */
public interface Scene {

    /**
     * Called each frame while the scene is active.
     *
     * @param eng active Engine instance
     * @param t total elapsed time in seconds
     * @param dt delta time for current frame in seconds
     */
    void update(Engine eng, float t, float dt);

    /**
     * Called each frame to render scene content.
     *
     * @param g active GraphicsInterface rendering context
     */
    void render(GraphicsInterface g);

    /**
     * Called once when the scene is first set/pushed onto the engine scene stack.
     *
     * @param eng active Engine instance
     */
    void start(Engine eng);

    /**
     * Called once when the scene is removed/popped from the engine scene stack.
     *
     * @param eng active Engine instance
     */
    void cleanup(Engine eng);

    /**
     * Called whenever this scene becomes active (after start or after popping top scene).
     *
     * @param eng active Engine instance
     */
    void resume(Engine eng);

    /**
     * Called whenever this scene becomes inactive (before pause or when pushing a new scene).
     *
     * @param eng active Engine instance
     */
    void pause(Engine eng);
}
