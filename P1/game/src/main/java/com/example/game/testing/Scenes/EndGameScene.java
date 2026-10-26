package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.getDefaultLevelGeneratorParams;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.TextPrefab;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.DistanceSystem;
import com.example.game.testing.Systems.levelGenerator.ProceduralLevelGenerator;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import org.w3c.dom.Text;

/**
 * This scene contains the meters and coins in text. And allows to return to the last game/level or return to the main menu
 */
public class EndGameScene extends mcd_Scene{
    private TextRectComponent metersText;
    private TextRectComponent coinsText;
    private boolean win;
    public EndGameScene(Engine myEngine, boolean win) {
        super(myEngine);
        this.win = win;
    }
    final static Vec2 pos = new Vec2(LOGIC_WINDOW_SIZE.x*0.1f, 0);
    final static Vec2 size = new Vec2(LOGIC_WINDOW_SIZE.x*0.4f, LOGIC_WINDOW_SIZE.y*0.15f);

    final static Vec2 buttonSize = DEFAULT_BUTTON_SIZE;
    final static float buttonYDelta = (LOGIC_WINDOW_SIZE.y-buttonSize.y*2)/3.0f;
    final static Vec2 buttonPos = new Vec2(pos.x+size.x+LOGIC_WINDOW_SIZE.x*0.1f, buttonYDelta);

    @Override
    public void start(Engine eng) {
        pos.y = LOGIC_WINDOW_SIZE.y * 0.10f;
        TextPrefab.createText(this,pos,size,win? "YOU WIN":"YOU LOSE", win?ColorLibrary.Blue : ColorLibrary.Red);
        pos.y = pos.y + size.y + LOGIC_WINDOW_SIZE.y*0.1f;
        coinsText = (TextRectComponent) TextPrefab.createText(this, pos,size, "0", ColorLibrary.Gold).getComponent(ComponentId.TEXT_COMPONENT);
        pos.y = pos.y + size.y + LOGIC_WINDOW_SIZE.y*0.1f;
        metersText = (TextRectComponent) TextPrefab.createText(this, pos,size, "0", ColorLibrary.Gold).getComponent(ComponentId.TEXT_COMPONENT);

        coinsText.setText("Coins: "+Integer.toString(CoinSystem.getInstance().getCoins()));
        metersText.setText("Distance: "+Integer.toString((int)DistanceSystem.getInstance().getDistance()/100));


        buttonPos.y = buttonYDelta;
        createButton(
            this,
            buttonPos,
            buttonSize,
            "Return to Main Menu",
            () ->{
                eng.setScene(new MainMenuScene(eng));
            }
        );
        buttonPos.y += buttonYDelta + buttonSize.y;
        createButton(
            this,
            buttonPos,
            buttonSize,
            "Play Again",
            () ->{
                MainGameScene mgs = new MainGameScene(eng);
                ProceduralLevelGenerator plg = new ProceduralLevelGenerator(mgs,getDefaultLevelGeneratorParams(), false);
                mgs.setLevelGen(plg);
                eng.setScene(mgs);
            }
        );
    }
}
