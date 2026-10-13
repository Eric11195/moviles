package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.LaserPrefab;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.LevelGeneratorParams;
import com.example.game.testing.Systems.PlayerCollisionDetector;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.utils.Vec2;

public class MainGameScene extends mcd_Scene{
    @Override
    public void start(Engine eng) {
        mcd_Entity player = PlayerPrefab.createPlayer(this,new Vec2(PLAYER_X_POS,100));
        LevelGeneratorParams lgp = new LevelGeneratorParams();
        lgp.levelDurationInSeconds = -1;
        lgp.levelInitialSpeed = 150;
        lgp.maxNumberOfSimultaneousObstacles = 4;
        lgp.obstacleExplosiveCreationBoost = 1.5f;
        lgp.obstacleExplosiveCreationChance = 0.000000001;
        lgp.obstacleLinearCreationPerSecond = 0.8f;
        addSystem(new SceneGenerator(this, player, lgp));
        addSystem(new PlayerCollisionDetector(player));
        //LaserPrefab.createLaser(this,0);
    }
}
