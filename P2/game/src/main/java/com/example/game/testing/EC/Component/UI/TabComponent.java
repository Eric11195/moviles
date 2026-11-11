package com.example.game.testing.EC.Component.UI;

import com.example.game.testing.EC.Component.DrawSingleRectComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.ColorLibrary;

import java.awt.Color;
import java.util.List;

public class TabComponent extends ButtonComponent{
    public static interface TabbedMenu{
        public void openTab(int idx);
    }
    public final int idx;
    public TabComponent(TabbedMenu myMenu, int idx){
        super(()->{
            myMenu.openTab(idx);
        });
        this.idx = idx;
    }
    boolean selected = false;
    public void select(mcd_Entity ent){
        selected = true;
        if(ent.hasComponent(ComponentId.DRAW_RECTANGLE_COMPONENT)) {
            DrawSingleRectComponent dr = (DrawSingleRectComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
            dr.color = ColorLibrary.Magenta;
        }
    }
    public void unselect(mcd_Entity ent){
        selected = false;
        if(ent.hasComponent(ComponentId.DRAW_RECTANGLE_COMPONENT)) {
            DrawSingleRectComponent dr = (DrawSingleRectComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
            dr.color = ColorLibrary.Red;
        }
    }
    @Override
    public void onClick(mcd_Entity ent){
        select(ent);
    }
}
