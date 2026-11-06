package com.example.game.testing.EC.Component.UI;

import java.util.List;

public abstract class TabComponent extends ButtonComponent{
    List<TabComponent> allTabs;
    int myIndex;
    TabComponent(int index){
        super(()->{});
        myIndex = index;
    }
    boolean selected = false;
    public void select(){
        selected = true;
        tabSelected(this.myIndex);
    }
    public void unselect(){
        selected = false;
    }
    abstract void tabSelected(int tabIdx);
    @Override
    public void onClick(){
        select();
    }
}
