package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public abstract class Obstacle implements mcd_Component {
    private float spawnTime;
    private float scrolledTime;
    public Obstacle(float spawnTime, float speedMult){
        this.spawnTime = spawnTime;
        scrolledTime = calculateScrolledTime(spawnTime, speedMult);
    }
    public abstract float calculateScrolledTime(float spawnTime, float speedMult);
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        if(!onScreen(t)) ent.setActive(false);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {}
    public boolean onScreen(float time){
        return scrolledTime > time;
    }
}
