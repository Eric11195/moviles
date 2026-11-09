package com.example.game.testing.EC.Component.UI;

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
    public void select(){
        selected = true;
        this.idleColor = ColorLibrary.Magenta;
    }
    public void unselect(){

        selected = false;
        this.idleColor = ColorLibrary.Red;
    }
    @Override
    public void onClick(){
        select();
    }
}
