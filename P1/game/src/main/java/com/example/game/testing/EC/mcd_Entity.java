package com.example.game.testing.EC;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class mcd_Entity {
    mcd_Scene myScene;
    private boolean alive = true;

    public mcd_Entity(mcd_Scene scene){
        myScene = scene;
    }
    private final List<mcd_Component> components =
            new ArrayList<>(
                    Collections.nCopies(
                            ComponentId.COMPONENT_COUNT.ordinal(),
                            (mcd_Component) null
                    )
            );
    public mcd_Component getComponent(ComponentId id){
        if(!hasComponent(id)) throw new RuntimeException("Cannot access non existing component");
        return components.get(getCmpIdx(id));
    }
    public boolean hasComponent(ComponentId id){
        return components.get(getCmpIdx(id)) != null;
    }

    public void addComponent(ComponentId id, mcd_Component cmp){
        if(hasComponent(id)) throw new RuntimeException("Component already exists new one cannot be created");
        components.set(getCmpIdx(id), cmp);
    }

    public mcd_Entity createNewEntity(){
        return myScene.createEntity();
    }
    //Gets the idx of the component with the given id in the
    private int getCmpIdx(ComponentId id){
        return id.ordinal();
    }
    //Removes the component
    public void rmComponent(ComponentId id){
        if(!hasComponent(id)) throw new RuntimeException("Component already exists new one cannot be created");
        components.set(getCmpIdx(id), null);
    }
    public void update(double dt){
        for(mcd_Component cmp : components){
            if(cmp!=null) cmp.update(this,dt);
        }
    }
    public void render(GraphicsInterface gi){
        for(mcd_Component cmp : components){
            if(cmp!=null) cmp.render(this,gi);
        }
    }
    public void destroyThisEntity(){
        alive = false;
    }
    public boolean getAlive(){
        return alive;
    }
    public Engine getEngine(){
        return myScene.getEngine();
    }
    public mcd_Scene getMyScene(){return myScene;}
}
