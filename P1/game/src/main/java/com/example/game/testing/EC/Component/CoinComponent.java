package com.example.game.testing.EC.Component;

import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_COIN;
import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_ZAPPER;
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
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class CoinComponent extends Obstacle {

    int value;

    mcd_Entity myEnt;

    public static class CoinComponentSerialized extends ObstacleFactory{
        Vec2 pos;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float t) {
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new CoinComponent(ent,t));
        }
    }
    public CoinComponent(mcd_Entity _ent ,float spawnTime, int _value) {
        super(_ent, spawnTime);
        myEnt = _ent;
        value = _value;
        scrolledTime = calculateScrolledTime(spawnTime);
    }

    public CoinComponent(mcd_Entity _ent ,float spawnTime) {
        super(_ent, spawnTime);
        myEnt = _ent;
        value = 1;
        scrolledTime = calculateScrolledTime(spawnTime);
    }


    @Override
    public float calculateScrolledTime(float spawnTime) {
        return spawnTime+ (LOGIC_WINDOW_SIZE.x + COIN_RADIUS*2)/LevelSpeedSystem.getInstance().getSpeedMult();
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t,float dt) {
        dm.drawSquare(tr.pos, tr.size);
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
        g.drawCircle(tr.pos,tr.size.x,true);
        g.setColor(ColorLibrary.DarkGold);
        g.drawCircle(tr.pos,tr.size.x,false);
    }


    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        TransformComponent tr = (TransformComponent) myEnt.getComponent(ComponentId.TRANSFORM_COMPONENT);
        jo.put("pos", JSON_HELPER.toJSON(tr.pos));
        jo.put("id",OBSTACLE_COIN);
        return jo;
    }
    public static CoinComponent.CoinComponentSerialized deserialize(JSONObject obj){
        CoinComponent.CoinComponentSerialized ccs = new CoinComponent.CoinComponentSerialized();
        ccs.pos = fromJSON((JSONObject) obj.getJSONObject("pos"));
        ccs.spawnTime = getSpawnTime(obj);
        return ccs;
    }
}
