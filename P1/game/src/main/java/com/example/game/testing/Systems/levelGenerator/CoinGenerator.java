package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.COIN_RADIUS;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_FALL_SPEED;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_UPWARDS_SPEED;

import com.example.game.testing.EC.Component.CoinComponent;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class CoinGenerator extends Generator{
    float lastCoinTimestamp = -1;
    Vec2 lastCoinPos = new Vec2(-1,-1);
    public CoinGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        super(_scene, _params,_dangerMatrix);
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt) {
        return createCoin(ent,t,dt);
    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {
        float probability = params.coinLinearCreationPerSecond*dt;
        obstacleAcumSpawn += probability;
    }

    @Override
    protected void initializeObstacleList(mcd_Scene scene) {
        _obstacleList = new ArrayList<mcd_Entity>(params.maxNumberOfSimultaneousCoins);
        for(int i = 0; i < params.maxNumberOfSimultaneousCoins; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        _obstacleArrayIndex = 0;
        _freeIdxCount = params.maxNumberOfSimultaneousCoins;
    }
    Vec2 dangerMatrixCoinPos = new Vec2(LOGIC_WINDOW_SIZE.x, 0);
    Vec2 dangerMatrixCoinSize = new Vec2(COIN_RADIUS*2,2*COIN_RADIUS);

    public boolean createCoin(mcd_Entity ent, float t, float dt){
        Vec2 preferredZone = new Vec2(0, LOGIC_WINDOW_SIZE.y);

        if(lastCoinTimestamp != -1) {
            // Optional: Break the chain if it's been too long since the last coin
            if(t - lastCoinTimestamp > 1.0f) {
                lastCoinTimestamp = -1;
            } else {
                float maxAllowedYMove = Math.min(MAX_PLAYER_UPWARDS_SPEED, MAX_PLAYER_FALL_SPEED);
                float diff = (t - lastCoinTimestamp) * 0.7f * maxAllowedYMove;
                preferredZone.x = Math.max(preferredZone.x, lastCoinPos.y - diff);
                preferredZone.y = Math.min(preferredZone.y, lastCoinPos.y + diff);
                if(preferredZone.y < preferredZone.x) return false;
            }
        }

        float y = preferredZone.x + (preferredZone.y - preferredZone.x) * (float)Math.random();

        if(y < COIN_RADIUS || y > LOGIC_WINDOW_SIZE.y - COIN_RADIUS) return false;

        dangerMatrixCoinPos.y = y - COIN_RADIUS;
        if(!dangerMatrix.checkRect(dangerMatrixCoinPos, dangerMatrixCoinSize)) return false;

        dangerMatrix.drawSquare(dangerMatrixCoinPos, dangerMatrixCoinSize);
        ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new CoinComponent(ent, t, y));
        lastCoinTimestamp = t;
        lastCoinPos.y = y;
        return true;
    }
}
