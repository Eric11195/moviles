package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.LevelGenerator.LevelFileGenerator;
import com.example.game.testing.Systems.menu.Scrollable;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Presents a selection of several levels. On pressing any of the buttons. It will play
 */
public class LevelSelectorScene extends mcd_Scene{
    LevelSelectorScene(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        int BUTTONS_PER_ROW = 3;
        Vec2 BASE_POS = new Vec2(LOGIC_WINDOW_SIZE.x/8,0);
        eng.getFileStorage().setRelativePath("levels");

        AtomicInteger idx = new AtomicInteger(0);
        ArrayList<mcd_Entity> buttons = new ArrayList<>();
        eng.getFileStorage().forEachFileOnDirectory((String filename)->{
            if (!filename.contains("Level")) return;
            JSONObject obj = JSON_HELPER.fromJSON(eng.getFileStorage(), "Level" + Integer.toString(idx.get() + 1));
            if (obj == null) {
                throw new RuntimeException("File level could not be found");
            }
            Vec2 pos = new Vec2(
                    0,0);
            LevelFileGenerator.LevelGeneratorSerialized lgs = LevelFileGenerator.deserialize(obj);
            int num = lgs.levelGeneratorParams.levelCoinsToWin == -1 ? lgs.levelGeneratorParams.levelTotalMeters : lgs.levelGeneratorParams.levelCoinsToWin;
            buttons.add (createButton(
                    this,
                    pos,
                    DEFAULT_BUTTON_SIZE,
                    (lgs.levelGeneratorParams.levelCoinsToWin == -1 ? "Distance" : "Coins") + ": " + num,
                    () -> {
                        loadLevel(eng, lgs);
                    }
            ));
            idx.incrementAndGet();
        });

        Scrollable scrollable = new Scrollable(BASE_POS,new Vec2(LOGIC_WINDOW_SIZE.x*6/8,LOGIC_WINDOW_SIZE.y),idx.get()/BUTTONS_PER_ROW,BUTTONS_PER_ROW,buttons);
        systemList.add(scrollable);

        createButton(
                this,
                new Vec2(0,LOGIC_WINDOW_SIZE.y-40),
                new Vec2(40,40),
                "Back",
                () ->{
                    eng.setScene(new MainMenuScene(eng));
                }
        );
    }

    private void loadLevel(Engine eng, LevelFileGenerator.LevelGeneratorSerialized lgs){
        MainGameScene mainScene = new MainGameScene(eng, lgs.levelGeneratorParams.levelCoinsToWin==-1? MainGameScene.Retos.distancia : MainGameScene.Retos.monedas);
        mainScene.setDistanceToWin(lgs.levelGeneratorParams.levelTotalMeters * 100);
        mainScene.setCoinsToWin(lgs.levelGeneratorParams.levelCoinsToWin);
        mainScene.setFixedLevelGen(
            lgs
        );
        eng.setScene(mainScene);
    }
}
