package com.example.mochilacohetonadisfrutona.game;

import static com.example.mochilacohetonadisfrutona.game.testing.testForScrapping.renderTest;

import com.example.mochilacohetonadisfrutona.intermedium.Engine;

public class MainGame {

    public void executeGameLoop(Engine eng){
        start(eng);
        while(update(eng)){}
        cleanup(eng);
    }

    private void start(Engine eng){
        renderTest(eng);
    }
    //Returns true if the loop should continue, false otherwise
    private boolean update(Engine eng){
        return eng.getRunning();
    }
    private void cleanup(Engine eng){

    }
}
