package com.example.desktop;

import com.example.desktopengine.DesktopEngine;
import com.example.game.testing.TestScene;

public class MainDesktop {
    public static void main(String[] args){
        DesktopEngine eng = new DesktopEngine(800,600);
        eng.setScene(new TestScene());
        eng.resume();
    }
}
