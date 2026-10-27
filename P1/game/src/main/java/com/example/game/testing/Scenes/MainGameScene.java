package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.BackgroundComponent;
import com.example.game.testing.EC.Component.DrawRectangleComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.CoinSystem;
import com.example.game.testing.Systems.DistanceSystem;
import com.example.game.testing.Systems.EndGameAfterXCoins;
import com.example.game.testing.Systems.EndGameAfterXDistance;
import com.example.game.testing.Systems.GameDistanceToEndOfLevelRenderer;
import com.example.game.testing.Systems.levelGenerator.BaseLevelGenerator;
import com.example.game.testing.Systems.PlayerCollisionDetector;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

/**
 * Play the game on this scene, either a preset level or a procedurally generated one
 */
public class MainGameScene extends mcd_Scene{
    BaseLevelGenerator levelGen = null;
    float gameTime;

    public static enum Retos{
        none(0),
        monedas(1),
        distancia(1<<1);

        Retos(int i) {
            this.value = i;
        }
        int getValue(){
            return this.value;
        }
        boolean is(Retos compareTo){
            return (this.value & compareTo.value) == compareTo.value;
        }
        int value;
    }

    Retos retos;
    protected MainGameScene(Engine myEngine) {
        super(myEngine);
        retos = Retos.none;

        createBackground();
    }
    protected MainGameScene(Engine myEngine, Retos r){
        super(myEngine);
        this.retos = r;

        createBackground();
    }
    private int coinsToWin;
    public void setCoinsToWin(int coins){
        this.coinsToWin = coins;
    }
    private float distanceToWin;
    public void setDistanceToWin(float distanceToWin){
        this.distanceToWin = distanceToWin;
    }

    public void createBackground (){
        mcd_Entity background0 = createEntity();
        background0.addComponent(ComponentId.BACKGROUND_COMPONENT, new BackgroundComponent(new Vec2(Constants.BACKGROUND_0_MIN_WIDTH,Constants.BACKGROUND_0_MIN_HEIGHT),
                new Vec2(Constants.BACKGROUND_0_MAX_WIDTH,Constants.BACKGROUND_0_MAX_HEIGHT),
                Constants.BACKGROUND_0_SPEED_SCALE,
                ColorLibrary.LightGrey));
        background0.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT,new DrawRectangleComponent());

        mcd_Entity background1 = createEntity();
        background1.addComponent(ComponentId.BACKGROUND_COMPONENT, new BackgroundComponent(new Vec2(Constants.BACKGROUND_1_MIN_WIDTH,Constants.BACKGROUND_1_MIN_HEIGHT),
                new Vec2(Constants.BACKGROUND_1_MAX_WIDTH,Constants.BACKGROUND_1_MAX_HEIGHT),
                Constants.BACKGROUND_1_SPEED_SCALE,
                ColorLibrary.DarkGrey));
        background1.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT,new DrawRectangleComponent());
    }
    public void setLevelGen(BaseLevelGenerator levelGen){
        if(this.levelGen!=null){
            throw new RuntimeException("You must not call setLevelGen again once levelGen is correctly set up");
        }
        this.levelGen = levelGen;
    }
    @Override
    public void update(Engine eng, float t, float dt){
        super.update(eng, gameTime, dt);
        gameTime += dt;
    }
    @Override
    public void start(Engine eng) {
        if(levelGen==null){
            throw new RuntimeException("You must call setLevelGen before this function");
        }
        gameTime = 0;

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
        CoinSystem cs = CoinSystem.getInstance();
        addSystem(cs);
        cs.reset();
        DistanceSystem ds = DistanceSystem.getInstance();
        addSystem(ds);
        ds.reset();
        addSystem(levelGen);

        addRetosSystems(eng);
    }
    private void addRetosSystems(Engine eng){
        boolean retoActivated = false;
        if(this.retos.is(Retos.monedas)){
            retoActivated=true;
            //add win by coin system
            addSystem(new EndGameAfterXCoins(coinsToWin));
            addSystem(new EndGameAfterXDistance(distanceToWin,false));
        }else if(this.retos.is(Retos.distancia)){
            retoActivated=true;
            //add win by distance system
            addSystem(new EndGameAfterXDistance(distanceToWin,true));
        }
        if(retoActivated){
            //add distance to goal system
            addSystem(new GameDistanceToEndOfLevelRenderer(distanceToWin));
        }
    }
}
