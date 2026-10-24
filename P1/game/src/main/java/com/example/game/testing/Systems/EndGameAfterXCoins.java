package com.example.game.testing.Systems;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.WinMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;

public class EndGameAfterXCoins implements mcd_System{
    int maxCoins;
    public EndGameAfterXCoins(int maxCoins){
        this.maxCoins = maxCoins;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        if(CoinSystem.getInstance().getCoins() >= maxCoins){
            Engine eng = scene.getEngine();
            eng.setScene(new WinMenuScene(eng));
        }
    }

    @Override
    public void render(GraphicsInterface g) {

    }
}
