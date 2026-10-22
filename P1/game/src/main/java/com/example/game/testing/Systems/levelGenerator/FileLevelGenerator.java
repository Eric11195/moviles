package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

import java.util.List;

public class FileLevelGenerator extends BaseLevelGenerator {
    public FileLevelGenerator(mcd_Scene scene, LevelFileGenerator.LevelGeneratorSerialized lgs){
        super(scene, lgs.levelGeneratorParams);
        generators.add(new FileObstacleGenerator(scene,lgs.levelGeneratorParams,new DangerMatrix(1),lgs.obsFactory));
    }
}
