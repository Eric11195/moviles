package com.example.game.testing.Systems;

import static com.example.game.testing.EC.Prefabs.PlayerPrefab.createPlayer;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;
import static com.example.game.testing.utils.ToJSON.toJSON;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.JetpackComponent;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.mcd_Serializable;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;

public class LevelGenerator implements mcd_Serializable {
    float levelInitialSpeed;
    float levelDurationInSeconds;
    SceneGenerator sg;
    ArrayList<Obstacle> obstacleCompleteList = new ArrayList<>();
    mcd_Scene myScene;
    JetpackComponent playerJetpack;
    mcd_Entity player;

    public static final float artificialDeltaTime = 0.16f;
    public static JSONObject generateNewLevel(mcd_Scene scene, Engine eng, LevelGeneratorParams params){
        float currentTime = 0;
        LevelGenerator lg = new LevelGenerator();
        lg.levelInitialSpeed = params.levelInitialSpeed;
        lg.levelDurationInSeconds = params.levelDurationInSeconds;
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
        while(currentTime < params.levelDurationInSeconds){
            lg.update(currentTime, artificialDeltaTime);
            currentTime += artificialDeltaTime;
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
        jo.put("levelDurationInSeconds", levelDurationInSeconds);
        jo.put("ObstacleList", toJSON(obstacleCompleteList));
        return jo;
    }
}
