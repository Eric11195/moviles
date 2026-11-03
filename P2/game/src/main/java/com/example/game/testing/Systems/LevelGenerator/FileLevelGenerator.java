package com.example.game.testing.Systems.LevelGenerator;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PX_TILE_SIZE;

import com.example.game.testing.Scenes.mcd_Scene;

public class FileLevelGenerator extends BaseLevelGenerator {
    public FileLevelGenerator(mcd_Scene scene, LevelFileGenerator.LevelGeneratorSerialized lgs){
        super(scene, lgs.levelGeneratorParams);
        generators.add(new FileObstacleGenerator(scene,lgs.levelGeneratorParams,new DangerMatrix(DANGER_MATRIX_PX_TILE_SIZE),lgs.obsFactory));
    }
}
