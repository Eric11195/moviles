package com.example.engine;

public interface Scene {
    /**
     * Called each frame while scene is the one marked as active
     * @param eng
     * @param t
     * @param dt
     */
    void update(Engine eng, float t, float dt);

    /**
     * Called each frame while scene is the one marked as active
     * @param g
     */
    void render(GraphicsInterface g);

    /**
     * Called once when the scene is added to the engine
     * @param eng
     */
    void start(Engine eng);

    /**
     * Called once when the scene is erased from the engine
     * @param eng
     */
    void cleanup(Engine eng);

    /**
     * Called each time this scene becomes active. This is called after start
     * @param eng
     */
    void resume(Engine eng);

    /**
     * Called each time this scene becomes unactive. This is called before cleanup
     * @param eng
     */
    void pause(Engine eng);
}
