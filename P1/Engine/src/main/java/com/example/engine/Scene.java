package com.example.engine;

public interface Scene {
    void update(Engine eng, float t, float dt);
    void render(GraphicsInterface g);
    void start(Engine eng);
    void cleanup(Engine eng);
}
