package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MISSILE_MOVE_SPEED;
import static com.example.game.testing.constants.Constants.MISSILE_RADIUS;
import static com.example.game.testing.constants.Constants.WARNING_TIME;
import static com.example.game.testing.utils.StartPlayerDead.startPlayerDead;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.utils.Vec2;

public class MissileComponent extends WarnedObstacle {
    float xSpeed;

    /**
     *
     * @param spawnTime
     * @param speedMult how many px the scenery moves per frame
     */
    public MissileComponent(mcd_Entity ent, float spawnTime, float speedMult) {
        super(ent, spawnTime, speedMult);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        xSpeed = vel.velocity.x - MISSILE_MOVE_SPEED;
        vel.velocity.x=0;
        vel.velocity.y=0;
        generateCollider(ent);
        scrolledTime = calculateScrolledTime(spawnTime,speedMult);
    }

    @Override
    public void activateObstacle(mcd_Entity ent, float t, float dt) {
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        vel.velocity.x = xSpeed;
    }

    private void generateCollider(mcd_Entity ent){
        ColliderComponent col = (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        col.clearColliders();
        col.addSingleCollider(new Vec2(0,0), MISSILE_RADIUS);
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime + WARNING_TIME + LOGIC_WINDOW_SIZE.x/(speedMult+MISSILE_MOVE_SPEED);
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
        float realXPos = tr.pos.x - getTimeTillSpawn() * xSpeed;
        dm.drawSquare(new Vec2(realXPos,tr.pos.y-tr.size.y), tr.size);
    }

    @Override
    public void onPlayerCollision(mcd_Entity ent, mcd_Scene scene) {
        startPlayerDead(scene.getEngine(), ent);
    }
}
