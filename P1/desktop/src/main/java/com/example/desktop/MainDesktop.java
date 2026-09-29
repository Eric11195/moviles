package com.example.desktop;

import com.example.desktopengine.DesktopEngine;
import com.example.game.testing.Scenes.MainMenuScene;

public class MainDesktop {
    public static void main(String[] args){
        DesktopEngine eng = new DesktopEngine(800,600);
        eng.setScene(new MainMenuScene());
        eng.resume();
    }
}
