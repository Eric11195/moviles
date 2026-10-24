package com.example.game.testing.Systems;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.WinMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

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
    Vec2 pos = new Vec2(LOGIC_WINDOW_SIZE.x-50, CoinSystem.pos.y);
    Vec2 size = new Vec2(50,CoinSystem.size.y);
    @Override
    public void render(GraphicsInterface g) {
        //render /60 (coins to win)
        String text = "/"+String.valueOf(maxCoins);
        g.setColor(ColorLibrary.Gold);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.HEADER));
        g.drawText(text, pos,size);
    }
}
