package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.TextPrefab;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.DistanceSystem;
import com.example.game.testing.Systems.LevelsSystem;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.SoundLibrary;
import com.example.utils.Vec2;

/**
 * This scene contains the meters and coins in text. And allows to return to the last game/level or return to the main menu
 */
public class EndGameScene extends mcd_Scene{
    private TextRectComponent metersText;
    private TextRectComponent coinsText;
    private boolean win;
    public EndGameScene(Engine myEngine, mcd_Scene levelScene, boolean win) {
        super(myEngine);
        this.win = win;
        this.levelScene = levelScene;
    }
    final static Vec2 pos = new Vec2(LOGIC_WINDOW_SIZE.x*0.1f, 0);
    final static Vec2 size = new Vec2(LOGIC_WINDOW_SIZE.x*0.4f, LOGIC_WINDOW_SIZE.y*0.15f);

    final static Vec2 buttonSize = DEFAULT_BUTTON_SIZE;
    final static float buttonYDelta = (LOGIC_WINDOW_SIZE.y-buttonSize.y*2)/3.0f;
    final static Vec2 buttonPos = new Vec2(pos.x+size.x+LOGIC_WINDOW_SIZE.x*0.1f, buttonYDelta);

    boolean gotExtra = false;

    mcd_Scene levelScene;

    @Override
    public void start(Engine eng) {
        gotExtra = false;
        pos.y = LOGIC_WINDOW_SIZE.y * 0.10f;
        eng.getAudio().stopSound(SoundLibrary.getSound(SoundLibrary.SoundsId.PLAYING));
        eng.getAudio().playSound(SoundLibrary.getSound(win? SoundLibrary.SoundsId.WIN:SoundLibrary.SoundsId.LOSE));
        TextPrefab.createText(this,pos,size,win? "YOU WIN":"YOU LOSE", win?ColorLibrary.Blue : ColorLibrary.Red, FontManager.Fonts.TITLE_FONT);
        pos.y = pos.y + size.y + LOGIC_WINDOW_SIZE.y*0.1f;
        coinsText = (TextRectComponent) TextPrefab.createText(this, pos,size, "0", ColorLibrary.Gold, FontManager.Fonts.COIN_N_DISTANCE_COUNT).getComponent(ComponentId.TEXT_COMPONENT);
        pos.y = pos.y + size.y + LOGIC_WINDOW_SIZE.y*0.1f;
        metersText = (TextRectComponent) TextPrefab.createText(this, pos,size, "0", ColorLibrary.Gold, FontManager.Fonts.COIN_N_DISTANCE_COUNT).getComponent(ComponentId.TEXT_COMPONENT);

        coinsText.setText("Coins: "+Integer.toString(CoinSystem.getInstance().getCoins()));
        metersText.setText("Distance: "+Integer.toString((int)DistanceSystem.getInstance().getDistance()/100));
        int rubiesGained = (int)(DistanceSystem.getInstance().getDistance()/10)+(int)(CoinSystem.getInstance().getCoins()/2);
        if(win&&!LevelsSystem.checkCompleted()){
            RubySystem.gainRubies(rubiesGained);
            LevelsSystem.completedLevel();
        }


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
                levelScene.cleanup(eng);
                eng.setScene(levelScene);
            }
        );

        if(win){
        buttonPos.y += buttonYDelta/5 + buttonSize.y;
        buttonSize.y = buttonSize.y*2;
        buttonSize.x = buttonSize.x*2;
        createButton(
                this,
                buttonPos,
                buttonSize,
                "WATCH AD FOR EXTRA RUBIES",
                () ->{
                    if(!gotExtra){
                    RubySystem.gainRubies(rubiesGained);
                    gotExtra = true;
                    }
                }
        );
        }
    }
}
