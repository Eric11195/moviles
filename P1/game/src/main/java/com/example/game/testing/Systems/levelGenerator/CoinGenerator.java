package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.COIN_RADIUS;
import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.game.testing.EC.Component.CoinComponent;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class CoinGenerator extends Generator{
    public CoinGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        super(_scene, _params,_dangerMatrix);
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt) {
        createCoin(ent,t,dt);
        return true;
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

    public boolean createCoin(mcd_Entity ent, float t, float dt){
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        LASER_HEIGHT*1.5f
                );
        TransformComponent tr =(TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        float y = space.x + (space.y-space.x)*(float)Math.random();
        tr.pos.x = LOGIC_WINDOW_SIZE.x + COIN_RADIUS;
        tr.pos.y = y;
        tr.size.x = tr.size.y = COIN_RADIUS;

        ColliderComponent collider = (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);

        collider.clearColliders();
        collider.addSingleCollider(new Vec2(0,0),COIN_RADIUS);

        CoinComponent coin = (CoinComponent) ent.setComponent(ComponentId.OBSTACLE_COMPONENT
                ,new CoinComponent(ent,t));
        coin.drawSelfInDangerMatrix((TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT), dangerMatrix,t,dt);

        return true;
    }
}
