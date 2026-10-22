package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

import java.util.ArrayList;
import java.util.List;

public class FileObstacleGenerator extends Generator{
    int current_idx;
    List<ObstacleFactory> obstaclesBeComing;
    public FileObstacleGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix, List<ObstacleFactory> obstacles) {
        super(_scene, _params, _dangerMatrix);
        obstaclesBeComing = obstacles;
        current_idx = 0;
    }

    @Override
    protected boolean askIfSpawnNewObstacle(float t, float dt) {
        return current_idx < obstaclesBeComing.size() && t >= obstaclesBeComing.get(current_idx).spawnTime;
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt, float speedMult) {
        obstaclesBeComing.get(current_idx).instantiate(ent.getMyScene(), ent, speedMult, t);
        ++current_idx;
        return true;
    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {

    }

    @Override
    protected void initializeObstacleList(mcd_Scene scene){
        _obstacleList = new ArrayList<mcd_Entity>(params.maxNumberOfSimultaneousObstacles);
        for(int i = 0; i < params.maxNumberOfSimultaneousObstacles; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
            ent.addComponent(ComponentId.WARNING_COMPONENT, new WarningSign());
            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        _obstacleArrayIndex = 0;
        _freeIdxCount = params.maxNumberOfSimultaneousObstacles;
    }
}
