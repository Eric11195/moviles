package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.mcd_Entity;

public class HazardGenerator extends Generator{
    @Override
    protected boolean askIfSpawnNewObstacle(float t, float dt) {
        return obstacleAcumSpawn > 1 && _freeIdxCount > 0;
    }

    @Override
    protected boolean setUpNewObstacle(mcd_Entity ent, float t, float dt) {
        return false;
    }

    @Override
    protected void updateObstacleSpawnConditions(float t, float dt) {

    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {

    }
}
