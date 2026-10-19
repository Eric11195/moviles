package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;

import java.util.ArrayList;

public abstract class Generator {
    public ArrayList<mcd_Entity> _obstacleList;
    public int _obstacleArrayIndex;
    protected int _freeIdxCount;
    float obstacleAcumSpawn = 0;

    public void setupDangerMatrixWithAlreadyExistingObjects(float t, float dt, DangerMatrix dangerMatrix){
        for(mcd_Entity ent : _obstacleList){
            if(!ent.isActive()) continue;
            ((Obstacle)ent.getComponent(ComponentId.OBSTACLE_COMPONENT)).drawSelfInDangerMatrix(
                    (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT),
                    dangerMatrix, t,  dt
            );
        }
    }
    public void update(float t, float dt){
        int i = _obstacleArrayIndex;
        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
        updateActiveObstacleCount();
        updateObstacleSpawnConditions(t,dt);

        while(!_obstacleList.get(i).isActive()){
            mcd_Entity ent = _obstacleList.get(i);
            assert(ent!=null);
            //if we create a new obstacle we try to repeat the process for the next free
            //Otherwise, we dont want to create new obstacles this frame
            if(!decideOnPossibleNewObstacle(ent, t, dt)) break;
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
    public boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt){
        if(askIfSpawnNewObstacle(t,dt)){
            //System.out.println("Creame un obstaculo pibe");
            if(setUpNewObstacle(_obstacleList.get(_obstacleArrayIndex), t, dt)) {
                _obstacleArrayIndex = (_obstacleArrayIndex + 1) % _obstacleList.size();
                return true;
            }
        }
        return false;
    }
    protected abstract boolean askIfSpawnNewObstacle(float t, float dt);
    protected abstract boolean setUpNewObstacle(mcd_Entity ent, float t, float dt);
    protected abstract void updateObstacleSpawnConditions(float t, float dt);
    public abstract void addProbabilityToObstacleSpawn(float dt);
}
