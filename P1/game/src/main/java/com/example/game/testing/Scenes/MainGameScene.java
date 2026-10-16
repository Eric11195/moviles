package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.Engine;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.levelGenerator.ILevelGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;
import com.example.game.testing.Systems.PlayerCollisionDetector;
import com.example.game.testing.Systems.levelGenerator.ProceduralLevelGenerator;
import com.example.utils.Vec2;

public class MainGameScene extends mcd_Scene{
    ILevelGenerator levelGen = null;

    protected MainGameScene(Engine myEngine) {
        super(myEngine);
    }

    public void setLevelGen(ILevelGenerator levelGen){
        if(this.levelGen!=null){
            throw new RuntimeException("You must not call setLevelGen again once levelGen is correctly set up");
        }
        this.levelGen = levelGen;
    }
    @Override
    public void start(Engine eng) {
        if(levelGen==null){
            throw new RuntimeException("You must call setLevelGen before this function");
        }
        int pauseButtonSize = 40;
        createButton(
            this,
            new Vec2(LOGIC_WINDOW_SIZE.x-pauseButtonSize,
                    0
            ),
            new Vec2(pauseButtonSize,pauseButtonSize),
            "||",
            ()->{
                eng.pushScene(new PauseMenu(eng, this));
            }
        );
        mcd_Entity player = PlayerPrefab.createPlayer(this);
        levelGen.setPlayer(player);
        addSystem(new PlayerCollisionDetector(player));
        addSystem(CoinSystem.getInstance());
        addSystem(levelGen);
    }
}
