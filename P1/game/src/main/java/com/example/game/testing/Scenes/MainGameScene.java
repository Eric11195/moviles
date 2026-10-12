package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.LaserPrefab;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.utils.Vec2;

public class MainGameScene extends mcd_Scene{
    @Override
    public void start(Engine eng) {
        mcd_Entity playerTransform = PlayerPrefab.createPlayer(this,new Vec2(PLAYER_X_POS,100));
        addSystem(new SceneGenerator(this, playerTransform, 4));
        //LaserPrefab.createLaser(this,0);
    }

    @Override
    public void update(Engine eng,float t, float dt){
        super.update(eng, t, dt);
    }
}
