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
    private boolean active;
    public void setActive(boolean active){assert(!isCorrupt());this.active = active;}
    public boolean isActive(){assert(!isCorrupt());return this.active;}

    public mcd_Entity(mcd_Scene scene){
        myScene = scene;
        setActive(true);
    }
    private final List<mcd_Component> components =
            new ArrayList<>(
                    Collections.nCopies(
                            ComponentId.COMPONENT_COUNT.ordinal(),
                            (mcd_Component) null
                    )
            );
    public mcd_Component getComponent(ComponentId id){
        assert(!isCorrupt());
        if(!hasComponent(id)) throw new RuntimeException("Cannot access non existing component");
        return components.get(getCmpIdx(id));
    }
    public boolean hasComponent(ComponentId id){
        assert(!isCorrupt());
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
        assert(!isCorrupt());
        if(!hasComponent(id)) throw new RuntimeException("Component already exists new one cannot be created");
        components.set(getCmpIdx(id), null);
    }
    public void update(float t, float dt){
        assert(!isCorrupt());
        if(this.active)
            for(mcd_Component cmp : components){
                if(cmp!=null) cmp.update(this,t,dt);
            }
    }
    public void render(GraphicsInterface gi){
        assert(!isCorrupt());
        if(this.active)
            for(mcd_Component cmp : components){
                if(cmp!=null) cmp.render(this,gi);
            }
    }
    public void destroyThisEntity(){
        assert(!isCorrupt());alive = false;
    }
    public boolean getAlive(){
        assert(!isCorrupt());return alive;
    }
    public Engine getEngine(){
        assert(!isCorrupt());return myScene.getEngine();
    }

    public boolean isCorrupt(){
        return myScene==null;
    }
    public void setAsCorrupt() {
        myScene = null;
    }
    public mcd_Scene getMyScene(){return myScene;}
}
