package com.example.game.testing.Scenes;

import com.example.engine.Engine;

public class ShopScene extends mcd_Scene{
    protected ShopScene(Engine myEngine) {
        super(myEngine);
    }

    @Override
    public void start(Engine eng) {
        //create an object that displays rubies
    }
    enum tabs{
        powerups,
        characterSkins,
        obstacleSkins,
        colorPalletes,
        menus,
        levels
    }
    private void showTab(tabs t){
        //hide all previous objects

        //show only the objects on this tab
    }
}
