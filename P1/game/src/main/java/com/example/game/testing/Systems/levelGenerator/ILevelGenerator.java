package com.example.game.testing.Systems.levelGenerator;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.mcd_System;

import java.util.ArrayList;

public abstract class ILevelGenerator implements mcd_System {
    protected LevelGeneratorParams params;
    protected int _freeIdxCount;
    public int _circularArrayIndex;
    protected float speedMult;
    mcd_Entity player = null;
    public void setPlayer(mcd_Entity player){
        this.player = player;
    }
    protected ILevelGenerator(mcd_Scene scene, LevelGeneratorParams params){
        this.params = params;
        this.speedMult = this.params.levelInitialSpeed;
        initializeObstacleList(scene);
    }
    public ArrayList<mcd_Entity> _obstacleList;    @Override
    public void update(mcd_Scene scene, float t, float dt){
        int i = _circularArrayIndex;
        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
        updateActiveObstacleCount();
        updateObstacleSpawnConditions(t,dt);

        while(!_obstacleList.get(i).isActive()){
            mcd_Entity ent = _obstacleList.get(i);
            assert(ent!=null);
            //if we create a new obstacle we try to repeat the process for the next free
            //Otherwise, we dont want to create new obstacles this frame
            if(!decideOnPossibleNewObstacle(ent, t, dt)) return;
            if(i==lastIterableElem) return;
            i = (i+1) % _obstacleList.size();
        }
    }
    @Override
    public void render(GraphicsInterface g){}
    protected abstract boolean askIfSpawnNewObstacle(float t, float dt);
    /**
     * Decides whether to create a new obstacle or not, and which type and position in the first case
     * @param ent entity
     * @param t time
     * @param dt delta time
     * @return true if new obstacle was created, false otherwise
     */
    private boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt){
        if(askIfSpawnNewObstacle(t,dt)){
            //System.out.println("Creame un obstaculo pibe");
            if(setUpNewObstacle(_obstacleList.get(_circularArrayIndex), t, dt)) {
                _circularArrayIndex = (_circularArrayIndex + 1) % _obstacleList.size();
                return true;
            }
        }
        return false;
    }
    protected abstract boolean setUpNewObstacle(mcd_Entity ent, float t, float dt);
    protected abstract void updateObstacleSpawnConditions(float t, float dt);
    public void updateActiveObstacleCount(){
        _freeIdxCount = 0;
        for(mcd_Entity ent : _obstacleList){
            _freeIdxCount += ent.isActive() ? 0 : 1;
        }
    }
    private void initializeObstacleList(mcd_Scene scene){
        _obstacleList = new ArrayList<mcd_Entity>(params.maxNumberOfSimultaneousObstacles);
        for(int i = 0; i < params.maxNumberOfSimultaneousObstacles; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        _circularArrayIndex =0;
        _freeIdxCount = params.maxNumberOfSimultaneousObstacles;
    }
}
