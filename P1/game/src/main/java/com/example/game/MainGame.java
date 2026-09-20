package com.example.game;

import static com.example.game.testing.testForScrapping.renderTest;

import com.example.engine.Engine;
import com.example.game.testing.testForScrapping;

public class MainGame {
    private static final double TARGET_UPS = 60.0;
    private static final double TIME_PER_UPDATE = 1.0 / TARGET_UPS;
    // Cap maximum accumulated time to 0.25 seconds to prevent blocks
    public void executeGameLoop(Engine eng){
        start(eng);
        long lastTime = System.nanoTime();
        while(eng.getRunning()) {
            long currentTime = System.nanoTime();
            // Convert elapsed nanoseconds to seconds
            double elapsedTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            update(eng, currentTime, elapsedTime);
            try {
                long sleepFor = Math.round((TIME_PER_UPDATE-elapsedTime) * 1000);
                if(sleepFor > 0)
                    Thread.sleep(sleepFor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cleanup(eng);
    }

    private void start(Engine eng){

    }
    //Returns true if the loop should continue, false otherwise
    private void update(Engine eng, double t, double dt){

        testForScrapping.renderTest(eng);
    }
    private void cleanup(Engine eng){

    }
}
