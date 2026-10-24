package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public abstract class Obstacle implements mcd_Component, mcd_Serializable {

    protected final float spawnTime;

    protected float scrolledTime;

    /**
     *
     * @param spawnTime
     */
    public Obstacle(mcd_Entity ent, float spawnTime){
        this.spawnTime = spawnTime;
        VelocityComponent vel =(VelocityComponent)ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        vel.velocity.x = -LevelSpeedSystem.getInstance().getSpeedMult();
        //EVERYONE MUST DO THIS ASSIGNMENT IT IS NOT AUTOMATIC
        //scrolledTime = calculateScrolledTime(spawnTime);
    }
    public abstract float calculateScrolledTime(float spawnTime);
    public abstract void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t, float dt);
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        VelocityComponent vel = (VelocityComponent)ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        vel.velocity.x = -LevelSpeedSystem.getInstance().getSpeedMult();
        if(!onScreen(t)) ent.setActive(false);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {}
    public boolean onScreen(float time){

        return scrolledTime > time;
    }


    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("spawnTime", Double.valueOf(spawnTime));
        return jo;
    }

    public static float getSpawnTime(JSONObject jo){
        return (float)jo.getDouble("spawnTime");
    }

    public abstract void onPlayerCollision(mcd_Entity player, mcd_Entity ent, mcd_Scene scene);

}
