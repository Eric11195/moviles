package com.example.game.testing.Systems.LevelGenerator;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PX_TILE_SIZE;

import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelGenerator.ObstacleGenerator.CoinGenerator;
import com.example.game.testing.Systems.LevelGenerator.ObstacleGenerator.HazardGenerator;

public class ProceduralLevelGenerator extends BaseLevelGenerator {
    //@param density marks the highest amount of obstacles in screen at the same time
    public ProceduralLevelGenerator(mcd_Scene scene, LevelGeneratorParams params, boolean simulatedOnly){
        super(scene, params);
        addGenerator(new HazardGenerator(scene, params, dangerMatrix));
        addGenerator(new CoinGenerator(scene, params, dangerMatrix));
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        updateDangerMatrix(t,dt);
        super.update(scene,t,dt);
    }

    private DangerMatrix dangerMatrix= new DangerMatrix(DANGER_MATRIX_PX_TILE_SIZE);
    public void setupDangerMatrixWithAlreadyExistingObjects(float t, float dt){
        generators.forEach(gnr ->
        {gnr.setupDangerMatrixWithAlreadyExistingObjects(t, dt);});
    }

    void updateDangerMatrix(float t, float dt){
        dangerMatrix.reset();
        setupDangerMatrixWithAlreadyExistingObjects(t,dt);
    }
}
