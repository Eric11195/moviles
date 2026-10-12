package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.DangerMatrix;

public abstract class Obstacle implements mcd_Component {
    private float spawnTime;
    protected float scrolledTime;
    private float speedMult;

    /**
     *
     * @param spawnTime
     * @param speedMult how many px the scenery moves per frame
     */
    public Obstacle(float spawnTime, float speedMult){
        this.spawnTime = spawnTime;
        this.speedMult = speedMult;
    }
    public abstract float calculateScrolledTime(float spawnTime, float speedMult);
    public abstract void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm);
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        if(!onScreen(t)) ent.setActive(false);
        assert(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT));
        TransformComponent tr = (TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos.x -= speedMult*dt;
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {}
    public boolean onScreen(float time){

        return scrolledTime > time;
    }

    public abstract void onPlayerCollision(mcd_Entity ent, mcd_Scene scene);

}
