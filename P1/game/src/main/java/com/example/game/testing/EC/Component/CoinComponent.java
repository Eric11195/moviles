package com.example.game.testing.EC.Component;

import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_COIN;
import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_ZAPPER;
import static com.example.game.testing.constants.Constants.COIN_COLLIDER_RADIUS;
import static com.example.game.testing.constants.Constants.COIN_RADIUS;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.utils.JSON_HELPER.fromJSON;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class CoinComponent extends Obstacle {

    int value;

    mcd_Entity myEnt;

    float halfsize = COIN_RADIUS/2;

    Vec2 halfsizes = new Vec2(halfsize,halfsize);

    public static class CoinComponentSerialized extends ObstacleFactory{
        float y;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float t) {
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new CoinComponent(ent,t, y));
        }
    }
    public CoinComponent(mcd_Entity _ent ,float spawnTime, int _value) {
        super(_ent, spawnTime);
        myEnt = _ent;
        value = _value;
        scrolledTime = calculateScrolledTime(spawnTime);
    }

    public CoinComponent(mcd_Entity _ent, float spawnTime, float y) {
        super(_ent, spawnTime);
        myEnt = _ent;
        value = 1;
        scrolledTime = calculateScrolledTime(spawnTime);
        TransformComponent tr =(TransformComponent) _ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos.x = LOGIC_WINDOW_SIZE.x + COIN_RADIUS;
        tr.pos.y = y;
        tr.size.x = tr.size.y = COIN_RADIUS;

        ColliderComponent collider = (ColliderComponent) _ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        collider.clearColliders();
        collider.addSingleCollider(new Vec2(0,0),COIN_COLLIDER_RADIUS);
    }

    @Override
    public float calculateScrolledTime(float spawnTime) {
        return spawnTime+ (LOGIC_WINDOW_SIZE.x + COIN_RADIUS*2)/LevelSpeedSystem.getInstance().getSpeedMult();
    }

    final static Vec2 drawInSelfMatrixPos = new Vec2(0,0);
    final static Vec2 drawInSelfMatrixSize = new Vec2(COIN_RADIUS*2,2*COIN_RADIUS);
    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t,float dt) {
        drawInSelfMatrixPos.x = tr.pos.x-COIN_RADIUS;
        drawInSelfMatrixPos.y = tr.pos.y-COIN_RADIUS;
        dm.drawSquare(drawInSelfMatrixPos, drawInSelfMatrixSize);
    }

    @Override
    public void onPlayerCollision(mcd_Entity player, mcd_Entity ent, mcd_Scene scene) {
        // ADD COIN
        CoinSystem.getInstance().addValue(value);
        ent.setActive(false);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        super.render(ent, g);
        g.setStrokeWidth(2);
        g.setColor(ColorLibrary.Gold);
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        Vec2 pos = tr.pos.add(halfsizes);
        g.drawCircle(pos,tr.size.x,true);
        g.setColor(ColorLibrary.DarkGold);
        g.drawCircle(pos,tr.size.x,false);
    }
//    @Override
//    public void update(mcd_Entity ent, float t, float dt){
//        super.update(ent, t, dt);
//        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
//        System.out.println("Coin ("+tr.pos.x+","+tr.pos.y+")");
//    }


    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        TransformComponent tr = (TransformComponent) myEnt.getComponent(ComponentId.TRANSFORM_COMPONENT);
        jo.put("y", (double) tr.pos.y);
        jo.put("id",OBSTACLE_COIN);
        return jo;
    }
    public static CoinComponent.CoinComponentSerialized deserialize(JSONObject obj){
        CoinComponent.CoinComponentSerialized ccs = new CoinComponent.CoinComponentSerialized();
        ccs.y = (float)obj.getDouble("y");
        ccs.spawnTime = getSpawnTime(obj);
        return ccs;
    }
}
