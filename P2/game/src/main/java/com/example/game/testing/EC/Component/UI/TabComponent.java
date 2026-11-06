package com.example.game.testing.EC.Component.UI;

import java.util.List;

public class TabComponent extends ButtonComponent{
    public static interface TabbedMenu{
        public void openTab(int idx);
    }
    public TabComponent(TabbedMenu myMenu, int idx){
        super(()->{
            myMenu.openTab(idx);
        });
    }
    boolean selected = false;
    public void select(){
        selected = true;
    }
    public void unselect(){
        selected = false;
    }
    @Override
    public void onClick(){
        select();
    }
}
