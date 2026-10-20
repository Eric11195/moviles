package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_MARGINS;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

import java.util.ArrayList;

public abstract class Generator {
    mcd_Entity player;
    DangerMatrix dangerMatrix;
    public ArrayList<mcd_Entity> _obstacleList;
    public int _obstacleArrayIndex;
    protected int _freeIdxCount;
    float obstacleAcumSpawn = 0;
    protected LevelGeneratorParams params;

    public Generator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        params = _params;
        initializeObstacleList(_scene);
        setDangerMatrix(_dangerMatrix);
    }
    public void setPlayer(mcd_Entity _player){
        player = _player;
    }
    public void setDangerMatrix (DangerMatrix _dangerMatrix){
        dangerMatrix = _dangerMatrix;
    }
    public void setupDangerMatrixWithAlreadyExistingObjects(float t, float dt){
        for(mcd_Entity ent : _obstacleList){
            if(!ent.isActive()) continue;
            ((Obstacle)ent.getComponent(ComponentId.OBSTACLE_COMPONENT)).drawSelfInDangerMatrix(
                    (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT),
                    dangerMatrix, t,  dt
            );
        }
    }
    public void update(float t, float dt, float speedMult){
        int i = _obstacleArrayIndex;
        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
        updateActiveObstacleCount();
        addProbabilityToObstacleSpawn(dt);
        setupDangerMatrixWithAlreadyExistingObjects(t,dt);

        while(!_obstacleList.get(i).isActive()){
            mcd_Entity ent = _obstacleList.get(i);
            assert(ent!=null);
            //if we create a new obstacle we try to repeat the process for the next free
            //Otherwise, we dont want to create new obstacles this frame
            if(!decideOnPossibleNewObstacle(ent, t, dt,speedMult)) break;
            if(i==lastIterableElem) break;
            i = (i+1) % _obstacleList.size();
        }
    }
    public void updateActiveObstacleCount(){
        _freeIdxCount = 0;
        for(mcd_Entity ent : _obstacleList){
            _freeIdxCount += ent.isActive() ? 0 : 1;
        }
    }
    public boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt, float speedMult){
        if(askIfSpawnNewObstacle(t,dt)){
            if(setUpNewObstacle(_obstacleList.get(_obstacleArrayIndex), t, dt, speedMult)) {
                _obstacleArrayIndex = (_obstacleArrayIndex + 1) % _obstacleList.size();
                return true;
            }
        }
        return false;
    }
    protected abstract boolean askIfSpawnNewObstacle(float t, float dt);
    protected abstract boolean setUpNewObstacle(mcd_Entity ent, float t, float dt, float speedMult);
    public abstract void addProbabilityToObstacleSpawn(float dt);

    public Vec2 getVerticalSafeZone(){
        return dangerMatrix.getFreeHeightRange(LOGIC_WINDOW_SIZE.x);
    }
    public Vec2 getSafeVerticalSpaceForObstacleSoPlayerCanPass(Vec2 freeYRange, float minObstacleHeight){
        float randomRange = (float)Math.random() * (LOGIC_WINDOW_SIZE.y-PLAYER_DIMENSIONS.y- PLAYER_MARGINS.y);
        if(randomRange < minObstacleHeight){
            //choose space under randomRange
            return new Vec2(randomRange, freeYRange.y);
        }else if(LOGIC_WINDOW_SIZE.y-randomRange < minObstacleHeight){
            //choose space above randomRange
            return new Vec2(freeYRange.x, randomRange);
        }else{
            //Choose either
            return (Math.random() > 0.5) ? new Vec2(freeYRange.x, randomRange) : new Vec2(randomRange, freeYRange.y);
        }
    }

    protected abstract void initializeObstacleList(mcd_Scene scene);
}
