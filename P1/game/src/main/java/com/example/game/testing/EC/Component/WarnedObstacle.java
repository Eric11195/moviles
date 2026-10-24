package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.WARNING_TIME;

import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;

public abstract class WarnedObstacle extends Obstacle{
    protected boolean activated=false;
    float timeTillSpawn;
    /**
     *
     * @param ent
     * @param spawnTime
     */
    public WarnedObstacle(mcd_Entity ent, float spawnTime) {
        super(ent, spawnTime);
        timeTillSpawn = WARNING_TIME;
        WarningSign ws = (WarningSign) ent.getComponent(ComponentId.WARNING_COMPONENT);
        ws.activateWarning();
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt){
        super.update(ent,t,dt);
        if(spawnTime + WARNING_TIME < t && !activated){
            activated = true;
            activateObstacle(ent, t, dt);
        }
        timeTillSpawn -= dt;
    }
    protected float getTimeTillSpawn(){
        return timeTillSpawn;
    }
    //This is called once, when the time from the warning is finished
    public void activateObstacle(mcd_Entity ent, float t, float dt){
        WarningSign ws = (WarningSign) ent.getComponent(ComponentId.WARNING_COMPONENT);
        ws.deactivateWarning();
    }
}
