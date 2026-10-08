package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

import java.util.ArrayList;

public class SceneGenerator implements mcd_System{
    int circularArrayIndex;
    int freeIdxCount;
    ArrayList<mcd_Entity> _obstacleList;
    //@param density marks the highest amount of obstacles in screen at the same time
    public SceneGenerator(mcd_Scene scene, int obstacleScreenDensity){
        initializeObstacleList(scene,obstacleScreenDensity);
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        int i = circularArrayIndex;
        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
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
    public void updateActiveObstacleCount(){
        freeIdxCount = 0;
        for(mcd_Entity ent : _obstacleList){
            freeIdxCount += ent.isActive() ? 0 : 1;
        }
    }
    @Override
    public void render(GraphicsInterface g){}

    /**
     * Decides whether to create a new obstacle or not, and which type and position in the first case
     * @param ent entity
     * @param t time
     * @param dt delta time
     * @return true if new obstacle was created, false otherwise
     */
    private boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt){
        circularArrayIndex = (circularArrayIndex+1) % _obstacleList.size();
        if(askIfSpawnNewObstacle(dt)){
            System.out.println("Creame un obstaculo pide");
            return true;
        }
        return false;
    }
    public boolean askIfSpawnNewObstacle(float dt){
        return freeIdxCount > 0 && (1 < freeIdxCount*dt*Math.random());
    }
    private void initializeObstacleList(mcd_Scene scene, int obstacleScreenDensity){
        _obstacleList = new ArrayList<mcd_Entity>(obstacleScreenDensity);
        for(int i = 0; i < obstacleScreenDensity; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        circularArrayIndex=0;
        freeIdxCount = obstacleScreenDensity;
    }
}
