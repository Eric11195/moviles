package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

public class CoinGenerator extends Generator{
    public CoinGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        super(_scene, _params,_dangerMatrix);
    }

    @Override
    protected boolean askIfSpawnNewObstacle(float t, float dt) {
        return false;
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt, float speedMult) {
        return false;
    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {

    }

    @Override
    protected void initializeObstacleList(mcd_Scene scene) {

    }
}
