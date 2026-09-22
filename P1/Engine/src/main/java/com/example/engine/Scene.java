package com.example.engine;

public interface Scene {
    void update(Engine eng, double dt);
    void render(GraphicsInterface g, double dt);
}
