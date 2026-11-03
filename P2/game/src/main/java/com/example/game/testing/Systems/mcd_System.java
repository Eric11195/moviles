package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;

public interface mcd_System {
    public void update(mcd_Scene scene, float t, float dt);
    public void render(GraphicsInterface g);

}
