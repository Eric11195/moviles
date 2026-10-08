package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.Scene;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.mcd_System;

import java.util.ArrayList;

public abstract class mcd_Scene implements Scene {
    protected ArrayList<mcd_Entity> entityList = new ArrayList<mcd_Entity>();
    protected ArrayList<mcd_System> systemList = new ArrayList<mcd_System>();
    private Engine my_engine;
    public Engine getEngine(){
        return my_engine;
    }
    @Override
    public void update(Engine eng, float t, float dt) {
        my_engine = eng;
        for(mcd_Entity ents : entityList){
            ents.update(t, dt);
        }
        for(mcd_System sys : systemList){
            sys.update(this, t,dt);
        }
        //erases entities that are no longer alive
        for(int i = entityList.size()-1; i>=0; --i){
            if(!entityList.get(i).getAlive()){
                entityList.get(i).setAsCorrupt();
                entityList.remove(i);
            }
        }
    }
    @Override
    public void render(GraphicsInterface g) {
        for(mcd_Entity ents : entityList){
            ents.render(g);
        }
        for(mcd_System sys : systemList){
            sys.render(g);
        }
    }
    @Override
    public void cleanup(Engine eng){
        entityList.clear();
    }

    public mcd_Entity createEntity(){
        entityList.add(new mcd_Entity(this));
        return entityList.get(entityList.size()-1);
    }
    public void addSystem(mcd_System sys){
        systemList.add(sys);
    }
}
