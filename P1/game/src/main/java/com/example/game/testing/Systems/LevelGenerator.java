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

    public static class LevelGeneratorParams{
        //Px speed per second. The obstacles move that much towards the left of the screen
        public float levelInitialSpeed;
        public float levelDurationInSeconds;
        public int maxNumberOfSimultaneousObstacles;
        /**
         * What are the chances of having an explosive obstacle creation
         * Moment in which the obstacle creator receives a huge boosts on tokens.
         */
        public double obstacleExplosiveCreationChance;
        /**
         * When obstacleExplosiveCreationChance happens.
         * How many token obstacles to add to the obstacle creator in that second. Float number.
         */
        public float obstacleExplosiveCreationBoost;
        /**
         * each second this amount of obstacle are created by default
         */
        public float obstacleLinearCreationPerSecond;
    }

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
        lg.playerJetpack = (JetpackComponent) lg.player.getComponent(ComponentId.JETPACK_COMPONENT);
        lg.sg = new SceneGenerator(
                scene,
                lg.player,
                params.maxNumberOfSimultaneousObstacles
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
