package com.example.game.testing.EC.Component;

import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_COIN;
import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_ZAPPER;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.utils.JSON_HELPER.fromJSON;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class CoinComponent extends Obstacle {

    int value;

    boolean active = true;

    mcd_Entity myEnt;

    public static class CoinComponentSerialized extends ObstacleFactory{
        Vec2 pos;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float levelSpeed, float t) {
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new CoinComponent(ent,t, levelSpeed));
        }
    }
    public CoinComponent(mcd_Entity _ent ,float spawnTime, float speedMult, int _value) {
        super(spawnTime, speedMult);
        myEnt = _ent;
        value = _value;
    }

    public CoinComponent(mcd_Entity _ent ,float spawnTime, float speedMult) {
        super(spawnTime, speedMult);
        myEnt = _ent;
        value = 1;
    }


    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime+ (LOGIC_WINDOW_SIZE.x)/speedMult;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
        dm.drawSquare(tr.pos, tr.size);
    }

    @Override
    public void onPlayerCollision(mcd_Entity ent, mcd_Scene scene) {
        // ADD COIN
        if (active) {
            CoinSystem.getInstance().addValue(value);
            active = false;
        }
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        super.render(ent, g);
        if (active){
            g.setColor(ColorLibrary.Gold);
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            g.drawCircle(tr.pos,tr.size.x/2,true);
        }
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
