package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

import java.util.List;

public class FileLevelGenerator extends BaseLevelGenerator {
    int current_idx;
    List<ObstacleFactory> obstaclesBeComing;

    public FileLevelGenerator(mcd_Scene scene, LevelFileGenerator.LevelGeneratorSerialized lgs){
        super(scene, lgs.levelGeneratorParams);
        obstaclesBeComing = lgs.obsFactory;
        current_idx = 0;
    }

    @Override
    protected boolean askIfSpawnNewObstacle(float t, float dt) {
        return current_idx < obstaclesBeComing.size() && t >= obstaclesBeComing.get(current_idx).spawnTime;
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt) {
        obstaclesBeComing.get(current_idx).instantiate(ent.getMyScene(), ent, speedMult, t);
        ent.setActive(true);
        ++current_idx;
        return true;
    }

    @Override
    protected void updateObstacleSpawnConditions(float t, float dt) {
    }
}
