package com.example.game.testing.EC;

import java.util.Arrays;
import java.util.List;

public class mcd_Entity {
    //add here the enum of your components
    public enum cmp_id{
        cmp_id_button
    }
    private List<mcd_Component> components = Arrays.asList(null);
    public mcd_Component getComponent(cmp_id id){
        if(!hasComponent(id)) throw new RuntimeException("Cannot access non existing component");
        return components.get(getCmpIdx(id));
    }
    public boolean hasComponent(cmp_id id){
        return components.get(getCmpIdx(id)) != null;
    }

    public void addComponent(cmp_id id, mcd_Component cmp){
        if(hasComponent(id)) throw new RuntimeException("Component already exists new one cannot be created");
        components.set(getCmpIdx(id), cmp);
    }
    //Gets the idx of the component with the given id in the
    private int getCmpIdx(cmp_id id){
        return id.ordinal();
    }
    //Removes the component
    public void rmComponent(cmp_id id){
        if(!hasComponent(id)) throw new RuntimeException("Component already exists new one cannot be created");
        components.set(getCmpIdx(id), null);
    }
}
