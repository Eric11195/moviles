package com.example.game.testing.EC.Component;

import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_MISSILE;
import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PRECALCULATED_SPACE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MISSILE_MOVE_SPEED;
import static com.example.game.testing.constants.Constants.MISSILE_RADIUS;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;
import static com.example.game.testing.constants.Constants.WARNING_TIME;
import static com.example.game.testing.utils.StartPlayerDead.startPlayerDead;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class MissileComponent extends WarnedObstacle {
    float xSpeed;
    TransformComponent myTransform;

    public static class MissileComponentSerialized extends ObstacleFactory {
        float yPos;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float levelSpeed, float t) {
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new MissileComponent(ent,t, levelSpeed, yPos));
        }
    }

    /**
     * @param spawnTime
     * @param speedMult how many px the scenery moves per frame
     */
    public MissileComponent(mcd_Entity ent, float spawnTime, float speedMult, float yPos) {
        super(ent, spawnTime, speedMult);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        xSpeed = vel.velocity.x - MISSILE_MOVE_SPEED;
        vel.velocity.x=0;
        vel.velocity.y=0;
        generateCollider(ent);
        setTransform(ent, yPos);
        scrolledTime = calculateScrolledTime(spawnTime,speedMult);
    }
    private void setTransform(mcd_Entity ent, float yPos){
        myTransform = (TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        myTransform.pos.x = LOGIC_WINDOW_SIZE.x;
        myTransform.pos.y = yPos;
        myTransform.size.x = myTransform.size.y = MISSILE_RADIUS*2;
    }

    @Override
    public void activateObstacle(mcd_Entity ent, float t, float dt) {
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        vel.velocity.x = xSpeed;
    }

    private void generateCollider(mcd_Entity ent){
        ColliderComponent col = (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        col.clearColliders();
        col.addSingleCollider(new Vec2(MISSILE_RADIUS,MISSILE_RADIUS), MISSILE_RADIUS);
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime + WARNING_TIME + (MISSILE_RADIUS*2 + LOGIC_WINDOW_SIZE.x)/(speedMult+MISSILE_MOVE_SPEED);
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
//        float realXPos = tr.pos.x - getTimeTillSpawn() * xSpeed;
//        if(realXPos < LOGIC_WINDOW_SIZE.x+DANGER_MATRIX_PRECALCULATED_SPACE) {
//            dm.drawSquare(new Vec2(realXPos, tr.pos.y - tr.size.y), tr.size);
//        }
        final float timeOfCollision = getTimeTillSpawn() + (LOGIC_WINDOW_SIZE.x-PLAYER_X_POS+PLAYER_DIMENSIONS.x)/(speedMult+MISSILE_MOVE_SPEED);
        float futureX = tr.pos.x - xSpeed * timeOfCollision;

        // Only consider the missile while it is relevant
        // to the predicted scene.
        if (futureX < LOGIC_WINDOW_SIZE.x+DANGER_MATRIX_PRECALCULATED_SPACE) {
            // Mark the missile's predicted position at time t.
            dm.drawSquare(
                    new Vec2(futureX, tr.pos.y),
                    new Vec2(MISSILE_RADIUS*2,MISSILE_RADIUS*2)
            );
        }
    }

    @Override
    public void onPlayerCollision(mcd_Entity ent, mcd_Scene scene) {
        startPlayerDead(scene.getEngine(), ent);
    }

    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        jo.put("y", Double.valueOf(myTransform.pos.y));
        jo.put("id", OBSTACLE_MISSILE);
        return jo;
    }
    public static MissileComponentSerialized deserialize(JSONObject obj){
        MissileComponentSerialized mcs = new MissileComponentSerialized();
        mcs.yPos = (float)obj.getDouble("y");
        mcs.spawnTime = getSpawnTime(obj);
        return mcs;
    }
    @Override
    public void update(mcd_Entity ent, float t, float dt){
        super.update(ent,t,dt);
    }
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g){
        super.render(ent, g);
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(ColorLibrary.Blue);
        g.drawRectangle(tr.pos, tr.size, true);
    }

}
