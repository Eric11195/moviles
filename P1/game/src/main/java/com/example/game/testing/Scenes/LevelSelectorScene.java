package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.Systems.levelGenerator.FileLevelGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelFileGenerator;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.SoundLibrary;
import com.example.utils.Vec2;

import org.json.JSONObject;

/**
 * Presents a selection of several levels. On pressing any of the buttons. It will play
 */
public class LevelSelectorScene extends mcd_Scene{
    LevelSelectorScene(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        int BUTTON_COUNT = 3;
        eng.getFileStorage().setRelativePath("assets/Levels");


        for(int j = 0; j < 2; ++j) {
            for (int i = 1; i <= BUTTON_COUNT; ++i) {
                int idx = BUTTON_COUNT*j+(i-1);
                Vec2 pos = new Vec2(
                        ((j+1)*LOGIC_WINDOW_SIZE.x / 3.0f) - 0.5f * DEFAULT_BUTTON_SIZE.x,
                        LOGIC_WINDOW_SIZE.y * ((float) i / (BUTTON_COUNT + 1.0f)) - 0.5f * DEFAULT_BUTTON_SIZE.y
                );
                JSONObject obj = JSON_HELPER.fromJSON(eng.getFileStorage(),"Level" + Integer.toString(idx+1));
                if(obj==null){
                    throw new RuntimeException("File level could not be found");
                }
                LevelFileGenerator.LevelGeneratorSerialized lgs = LevelFileGenerator.deserialize(obj);
                int num = lgs.levelGeneratorParams.levelCoinsToWin==-1 ? lgs.levelGeneratorParams.levelTotalMeters : lgs.levelGeneratorParams.levelCoinsToWin;
                createButton(
                    this,
                    pos,
                    DEFAULT_BUTTON_SIZE,
                    (lgs.levelGeneratorParams.levelCoinsToWin==-1 ? "Distance": "Coins")+": "+num,
                    () -> {
                        eng.getAudio().playSound(SoundLibrary.getSound(SoundLibrary.SoundsId.BUTTON));
                        loadLevel(eng, lgs);
                    }
                );
            }
        }
        createButton(
            this,
            new Vec2(0,LOGIC_WINDOW_SIZE.y-40),
            new Vec2(40,40),
            "Back",
            () ->{
                eng.getAudio().playSound(SoundLibrary.getSound(SoundLibrary.SoundsId.BUTTON));
                eng.setScene(new MainMenuScene(eng));
            }
        );
    }

    private void loadLevel(Engine eng, LevelFileGenerator.LevelGeneratorSerialized lgs){
        MainGameScene mainScene = new MainGameScene(eng, lgs.levelGeneratorParams.levelCoinsToWin==-1? MainGameScene.Retos.distancia : MainGameScene.Retos.monedas);
        mainScene.setDistanceToWin(lgs.levelGeneratorParams.levelTotalMeters * 100);
        mainScene.setCoinsToWin(lgs.levelGeneratorParams.levelCoinsToWin);
        mainScene.setLevelGen(
            new FileLevelGenerator(
                mainScene,
                lgs
            )
        );
        eng.setScene(mainScene);
    }
}
