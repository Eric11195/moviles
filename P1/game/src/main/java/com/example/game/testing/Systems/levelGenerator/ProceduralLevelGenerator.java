package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PX_TILE_SIZE;
import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_HEIGHT;
import static com.example.game.testing.constants.Constants.MISSILE_RADIUS;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_MARGINS;

import com.example.game.testing.EC.Component.MissileComponent;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.ObstacleId;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class ProceduralLevelGenerator extends BaseLevelGenerator {
    //@param density marks the highest amount of obstacles in screen at the same time
    public ProceduralLevelGenerator(mcd_Scene scene, LevelGeneratorParams params, boolean simulatedOnly){
        super(scene, params);
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        updateDangerMatrix(t,dt);
        super.update(scene,t,dt);
    }

    public DangerMatrix dangerMatrix= new DangerMatrix(DANGER_MATRIX_PX_TILE_SIZE);
    public void setupDangerMatrixWithAlreadyExistingObjects(float t, float dt){
        generators.forEach(gnr ->
        {gnr.setupDangerMatrixWithAlreadyExistingObjects(t, dt);});
    }

    void updateDangerMatrix(float t, float dt){
        dangerMatrix.reset();
        setupDangerMatrixWithAlreadyExistingObjects(t,dt);
    }
}
