package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.COLS_IN_SHOP;
import static com.example.game.testing.constants.Constants.ROWS_IN_SHOP;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.TabComponent;
import com.example.game.testing.EC.Prefabs.BackButtonPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.mcd_System;
import com.example.game.testing.Systems.menu.Scrollable;

import java.util.ArrayList;
import java.util.List;

public class ShopScene extends mcd_Scene implements TabComponent.TabbedMenu {
    protected ShopScene(Engine myEngine) {
        super(myEngine);
    }
    List<Scrollable> scrollableList;

    @Override
    public void start(Engine eng) {
        scrollableList = new ArrayList<>();
        //TODO: create an object that displays rubies
        //TODO: create a button for each tab

        //Creates all objects for all tabs
        for(int i = 0; i < tabs.count.ordinal(); ++i){
            // Open folder
            // Count number of elements
            int numberOfElementsInTab = 0;
            List<mcd_Entity> entOfThisTab = new ArrayList<>();
            for(int j = 0; j < numberOfElementsInTab; ++j){
                //Todo: create a shopObject
                //add to the entity list
                // DO NOT ADD TO THIS SCENE OBJECT LIST
                //transformsOfThisTab.add();
            }
            scrollableList.add(new Scrollable(ROWS_IN_SHOP, COLS_IN_SHOP, entOfThisTab));
        }
        openTab(tabs.powerups.ordinal());

        BackButtonPrefab.createBackButton(eng, this);
    }

    @Override
    public void openTab(int idx) {
        //Todo: set to the right color the previous tabs
        //TODO: hide all previous objects
        scrollableList.forEach((scrollable -> {
            scrollable.setActive(false);
        }));
        //TODO: show only the objects on this tab
        systemList.clear();
        Scrollable activeScrollable = scrollableList.get(idx);
        addSystem(activeScrollable);
        activeScrollable.setActive(true);
    }

    enum tabs{
        powerups,
        characterSkins,
        obstacleSkins,
        colorPalletes,
        menus,
        levels,
        count
    }
}
