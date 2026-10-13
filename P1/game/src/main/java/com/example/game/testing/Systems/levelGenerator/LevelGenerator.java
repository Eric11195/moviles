package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.EC.Prefabs.PlayerPrefab.createPlayer;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;
import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.JetpackComponent;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.ObstacleId;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.game.testing.utils.mcd_Serializable;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelGenerator implements mcd_Serializable {
    float levelInitialSpeed;
    float levelDurationInSeconds;
    int maxSimultaneousObstacleCount;
    SceneGenerator sg;
    ArrayList<Obstacle> obstacleCompleteList = new ArrayList<>();
    mcd_Scene myScene;
    JetpackComponent playerJetpack;
    mcd_Entity player;

    private static final float ARTIFICIAL_DELTA_TIME = 0.02f;
    public static JSONObject generateNewLevel(mcd_Scene scene, Engine eng, LevelGeneratorParams params){
        float currentTime = 0;
        LevelGenerator lg = new LevelGenerator();
        lg.levelInitialSpeed = params.levelInitialSpeed;
        lg.levelDurationInSeconds = params.levelDurationInSeconds;
        lg.maxSimultaneousObstacleCount = params.maxNumberOfSimultaneousObstacles;
        lg.myScene = scene;
        lg.player = createPlayer(
                scene,
                new Vec2(PLAYER_X_POS,LOGIC_WINDOW_SIZE.y-PLAYER_DIMENSIONS.y)
        );
        lg.player.rmComponent(ComponentId.PLAYER_INPUT_COMPONENT);
        lg.playerJetpack = (JetpackComponent) lg.player.getComponent(ComponentId.JETPACK_COMPONENT);
        lg.sg = new SceneGenerator(
                scene,
                lg.player,
                params
        );
        int loop_count = 0;
        while(currentTime < params.levelDurationInSeconds){
            lg.update(currentTime, ARTIFICIAL_DELTA_TIME);
            currentTime += ARTIFICIAL_DELTA_TIME;
            ++loop_count;
        }
        return lg.serialize();
    }
    private void update(float t, float dt){
        int index_before = this.sg._circularArrayIndex;
        this.sg.update(myScene, t, dt);
        this.playerJetpack.setIsFlying(Math.random() > 0.5f);
        this.player.update(t,dt);
        int index_after = this.sg._circularArrayIndex;
        while(index_before != index_after){
            this.obstacleCompleteList.add(
                    (Obstacle)
                    this.sg._obstacleList
                        .get(index_before)
                        .getComponent(ComponentId.OBSTACLE_COMPONENT)
            );
            index_before = (index_before+1) % this.sg._obstacleList.size();
        }
    }
    @Override
    public JSONObject serialize() {
        JSONObject jo = new JSONObject();
        jo.put("LevelSpeed",levelInitialSpeed);
        jo.put("LevelDurationInSeconds", levelDurationInSeconds);
        jo.put("ObstacleList", toJSON(obstacleCompleteList));
        jo.put("MaxSimultaneousObstacleCount", maxSimultaneousObstacleCount);
        return jo;
    }

    public static class LevelGeneratorSerialized {
        float levelInitialSpeed;
        float levelDurationInSeconds;
        int maxSimultaneousObstacles;
        List<ObstacleFactory> obsFactory = new ArrayList<ObstacleFactory>();
    }

    public static void deserialize(JSONObject obj) {
        LevelGeneratorSerialized lgp = new LevelGeneratorSerialized();
        lgp.levelInitialSpeed = obj.getFloat("LevelSpeed");
        lgp.levelDurationInSeconds = obj.getFloat("LevelDurationInSeconds");
        lgp.maxSimultaneousObstacles = obj.getInt("MaxSimultaneousObstacleCount");
        JSONArray ja = obj.getJSONArray("ObstacleList");
        for(int i = 0; i < ja.length(); ++i){
            JSONObject jo = ja.getJSONObject(i);
            switch (jo.getEnum(ObstacleId.class,"id")){
                case OBSTACLE_ZAPPER:
                    lgp.obsFactory.add(ZapperComponent.deserialize(jo));
                    break;
                case OBSTACLE_COIN:
                    break;
                case OBSTACLE_COUNT:
                    break;
                case OBSTACLE_LASER:
                    break;
                case OBSTACLE_MISSILE:
                    break;
                default:
                    throw new RuntimeException("That type of obstacle has a non valid id");
            }
        }
    }
}
