package com.example.game.testing.Systems;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.EndGameScene;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

public class EndGameAfterXDistance implements mcd_System{
    float maxDistance;
    boolean win;
    public EndGameAfterXDistance(float maxDistance, boolean win){
        this.maxDistance = maxDistance;
        this.win = win;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        if(DistanceSystem.getInstance().getDistance() >= maxDistance){
            Engine eng = scene.getEngine();
            eng.setScene(new EndGameScene(eng, this.win));
        }
    }
    Vec2 pos = new Vec2(DistanceSystem.pos.x+50, DistanceSystem.pos.y+5);
    Vec2 size = new Vec2(50,CoinSystem.size.y);
    @Override
    public void render(GraphicsInterface g) {
        //render /60 (coins to win)
        String text = "/"+String.valueOf((int)maxDistance/100);
        g.setColor(ColorLibrary.Red);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.HEADER));
        g.drawText(text, pos,size);
    }
}
