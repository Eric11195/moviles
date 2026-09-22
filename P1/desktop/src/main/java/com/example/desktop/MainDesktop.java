package com.example.desktop;

import com.example.desktopengine.DesktopEngine;
import com.example.game.MainGame;

public class MainDesktop {
    public static void main(String[] args){
        MainGame game = new MainGame();
        DesktopEngine eng = new DesktopEngine(800,600, game::executeGameLoop);
    }
}
