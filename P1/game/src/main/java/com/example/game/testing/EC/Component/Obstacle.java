package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public abstract class Obstacle implements mcd_Component, mcd_Serializable {
    protected final float spawnTime;
    protected float scrolledTime;
    protected float speedMult;

    /**
     *
     * @param spawnTime
     * @param speedMult how many px the scenery moves per frame
     */
    public Obstacle(mcd_Entity ent, float spawnTime, float speedMult){
        this.spawnTime = spawnTime;
        this.speedMult = speedMult;
        VelocityComponent vel =(VelocityComponent)ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        vel.velocity.x = -this.speedMult;
        //EVERYONE MUST DO THIS ASSIGNMENT IT IS NOT AUTOMATIC
        //scrolledTime = calculateScrolledTime(spawnTime, speedMult);
    }
    public abstract float calculateScrolledTime(float spawnTime, float speedMult);
    public abstract void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm);
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        if(!onScreen(t)) ent.setActive(false);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {}
    public boolean onScreen(float time){

        return scrolledTime > time;
    }


    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("spawnTime", Float.valueOf(spawnTime));
        return jo;
    }

    public static float getSpawnTime(JSONObject jo){
        return jo.getFloat("spawnTime");
    }

    public abstract void onPlayerCollision(mcd_Entity ent, mcd_Scene scene);

}
