package com.example.game;

import static com.example.game.testing.testForScrapping.renderTest;

import com.example.engine.Engine;
import com.example.game.testing.testForScrapping;

public class MainGame {

    public void executeGameLoop(Engine eng){
        start(eng);
        while(update(eng)){}
        cleanup(eng);
    }

    private void start(Engine eng){
        testForScrapping.renderTest(eng);
    }
    //Returns true if the loop should continue, false otherwise
    private boolean update(Engine eng){
        return eng.getRunning();
    }
    private void cleanup(Engine eng){

    }
}
