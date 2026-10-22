package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.utils.FontLibrary.FontName.STANDARD;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.mcd_System;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

import java.util.ArrayList;

public abstract class BaseLevelGenerator implements mcd_System {
    protected LevelGeneratorParams params;
    protected float speedMult;
    public ArrayList<Generator> generators = new ArrayList<>();

    mcd_Entity player = null;
    float distanceAdvanced = 0;
    public void setPlayer(mcd_Entity player){
        this.player = player;
        generators.forEach(gnr -> gnr.setPlayer(player));
    }
    protected BaseLevelGenerator(mcd_Scene scene, LevelGeneratorParams params){
        this.params = params;
        this.speedMult = this.params.levelInitialSpeed;
//        initializeObstacleList(scene);
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        generators.forEach(gnr->gnr.update(t,dt,speedMult));

//        int i = _obstacleArrayIndex;
//        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
//        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
//        updateActiveObstacleCount();
//        updateObstacleSpawnConditions(t,dt);
//
//        while(!_obstacleList.get(i).isActive()){
//            mcd_Entity ent = _obstacleList.get(i);
//            assert(ent!=null);
//            //if we create a new obstacle we try to repeat the process for the next free
//            //Otherwise, we dont want to create new obstacles this frame
//            if(!decideOnPossibleNewObstacle(ent, t, dt)) break;
//            if(i==lastIterableElem) break;
//            i = (i+1) % _obstacleList.size();
//        }

//        int j = _coinArrayIndex;
//        int lastIterableCoin = (j+_coinList.size()-1)% _coinList.size();
//        if(j >= _coinList.size()) throw new RuntimeException("This should never happen");
////        updateActiveObstacleCount();
////        updateObstacleSpawnConditions(t,dt);

        //TOMOVE
        // float toAdd = dt*speedMult;
        // if(((int)distanceAdvanced) / params.augmentSpeedEachXPixels != ((int)(distanceAdvanced+toAdd))/ params.augmentSpeedEachXPixels){
        //     speedMult += params.augmentSpeedByYEachXPixels;
        //     toAdd = dt*speedMult;
        // }
        // distanceAdvanced += toAdd;

//        updateActiveObstacleCount();
//        updateObstacleSpawnConditions(t,dt);
//        while(!_coinList.get(j).isActive()){
//            mcd_Entity ent = _coinList.get(j);
//            assert(ent!=null);
//            //if we create a new obstacle we try to repeat the process for the next free
//            //Otherwise, we dont want to create new obstacles this frame
//            if(!decideOnPossibleNewObstacle(ent, t, dt)) break;
//            if(j==lastIterableCoin) break;
//            j = (j+1) % _coinList.size();
//        }

    }
    Vec2 distanceTextPos = new Vec2(0,0);
    @Override
    public void render(GraphicsInterface g){
        g.setFont(FontLibrary.getFont(STANDARD));
        g.drawText(Integer.toString((int)this.distanceAdvanced), distanceTextPos);
    }
//    protected abstract boolean askIfSpawnNewObstacle(float t, float dt);
//    /**
//     * Decides whether to create a new obstacle or not, and which type and position in the first case
//     * @param ent entity
//     * @param t time
//     * @param dt delta time
//     * @return true if new obstacle was created, false otherwise
//     */
//    private boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt){
////        if(askIfSpawnNewObstacle(t,dt)){
//            //System.out.println("Creame un obstaculo pibe");
////            if(setUpNewObstacle(_obstacleList.get(_obstacleArrayIndex), t, dt)) {
////                _obstacleArrayIndex = (_obstacleArrayIndex + 1) % _obstacleList.size();
////                return true;
////            }
////        }
//        return false;
//    }
//    protected abstract boolean setUpNewObstacle(mcd_Entity ent, float t, float dt);
//    protected abstract void updateObstacleSpawnConditions(float t, float dt);
//    public void updateActiveObstacleCount(){
////        _freeIdxCount = 0;
////        for(mcd_Entity ent : _obstacleList){
////            _freeIdxCount += ent.isActive() ? 0 : 1;
////        }
//    }
//    private void initializeObstacleList(mcd_Scene scene){
////        _obstacleList = new ArrayList<mcd_Entity>(params.maxNumberOfSimultaneousObstacles);
////        for(int i = 0; i < params.maxNumberOfSimultaneousObstacles; ++i){
////            mcd_Entity ent = scene.createEntity();
////            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
////            ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
////            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
////            ent.addComponent(ComponentId.WARNING_COMPONENT, new WarningSign());
////            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
////            ent.setActive(false);
////            _obstacleList.add(ent);
////        }
////        _obstacleArrayIndex = 0;
////        _freeIdxCount = params.maxNumberOfSimultaneousObstacles;
////
////        _coinList = new ArrayList<mcd_Entity>(Constants.MAX_COINS);
////        for (int i = 0; i < Constants.MAX_COINS;++i){
////            mcd_Entity ent = scene.createEntity();
////            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
////            ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
////            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
////            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
////            ent.setActive(false);
////            _coinList.add(ent);
////        }
////        _coinArrayIndex = 0;
//    }
}
