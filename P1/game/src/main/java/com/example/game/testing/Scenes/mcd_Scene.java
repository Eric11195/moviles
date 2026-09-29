package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.Scene;
import com.example.game.testing.EC.mcd_Entity;

import java.util.ArrayList;

public abstract class mcd_Scene implements Scene {
    protected ArrayList<mcd_Entity> entityList = new ArrayList<mcd_Entity>();
    private Engine my_engine;
    public Engine getEngine(){
        return my_engine;
    }
    @Override
    public void update(Engine eng, double dt) {
        my_engine = eng;
        for(mcd_Entity ents : entityList){
            ents.update(dt);
        }
        //erases entities that are no longer alive
        for(int i = entityList.size()-1; i>=0; --i){
            if(!entityList.get(i).getAlive()){
                entityList.remove(i);
            }
        }
    }
    @Override
    public void render(GraphicsInterface g, double dt) {
        for(mcd_Entity ents : entityList){
            ents.render(g);
        }
    }

    public mcd_Entity createEntity(){
        entityList.add(new mcd_Entity(this));
        return entityList.get(entityList.size()-1);
    }
}
