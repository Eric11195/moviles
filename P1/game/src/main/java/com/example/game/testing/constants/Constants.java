package com.example.game.testing.constants;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;
import com.example.utils.Vec2;


public class Constants {
    public static final Vec2 LOGIC_WINDOW_SIZE = new Vec2(600,400);

    public static final int PLAYER_X_POS =(int) LOGIC_WINDOW_SIZE.x/5;
    public static final Vec2 PLAYER_DIMENSIONS= new Vec2(LOGIC_WINDOW_SIZE.x/12, LOGIC_WINDOW_SIZE.y/10);
    //PLAYER
    public static final float PLAYER_STARTING_Y_POS = LOGIC_WINDOW_SIZE.y-PLAYER_DIMENSIONS.y;
    public static final Vec2 PLAYER_MARGINS = new Vec2(PLAYER_DIMENSIONS.x, PLAYER_DIMENSIONS.y);
    //Vertical upwards
    public static final float MAX_PLAYER_UPWARDS_SPEED = 200;
    public static final float Y_PLAYER_ACCEL = 600;
    public static final float GRAVITY_FORCE = 300;
    public static final float MAX_PLAYER_FALL_SPEED = 300;

    //UI
    public static final Vec2 DEFAULT_BUTTON_SIZE = new Vec2(LOGIC_WINDOW_SIZE.x*0.25f,LOGIC_WINDOW_SIZE.y*0.15f);

    //OBSTACLES
    public static final int DANGER_MATRIX_PRECALCULATED_SPACE = (int)(LOGIC_WINDOW_SIZE.x * 0.3f);
        // SCENE GENERATOR
        public static final int DANGER_MATRIX_PX_TILE_SIZE = 10;

        // ZAPPERS
        public static final float MIN_ZAPPER_WIDTH = 60;
        public static final float MAX_ZAPPER_WIDTH = DANGER_MATRIX_PRECALCULATED_SPACE *0.6f;
        public static final float ZAPPER_DELTA_WIDTH = MAX_ZAPPER_WIDTH - MIN_ZAPPER_WIDTH;
        public static final float MIN_ZAPPER_HEIGHT = 10;
        public static final float ZAPPER_HEAD_RADIUS = 16;
        public static final float ZAPPER_LINE_WIDTH = 10;
        public static final int ZAPPER_MAX_COLLIDERS = 10;
        public static final float MAX_ZAPPER_LENGTH = 200;
        public static final float MIN_ZAPPER_TURN_RATE = 1f;
        public static final float MAX_ZAPPER_TURN_RATE = 2f;

        // MISILES
        public static final float MISSILE_MOVE_SPEED = LOGIC_WINDOW_SIZE.x*2;
        public static final float MISSILE_RADIUS = 30;

        // LASER
        public static final float LASER_HEIGHT = LOGIC_WINDOW_SIZE.y/5.0f;

        // COIN
        public static final float COIN_RADIUS = 10;
        public static final float MAX_COIN_Y_JUMP = COIN_RADIUS * 1.0f;
        public static final float COIN_COLLIDER_RADIUS = 12;
    /**
     * How long until an obstacle with warning appears from the first time it's hinted on screen
     */
    public static final float WARNING_TIME = 2;
    public static final float WARNING_RADIUS = 30;

    public static final float LASER_WIDTH = LOGIC_WINDOW_SIZE.x;
    public static final float LASER_ANIM_TIME = 0.5f;
    public  static final int LASER_ANIM_NUM = 4;
    public static final float LASER_ATTACK_DUR= 1.0f;

    // BACKGROUND
    public static final float BACKGROUND_1_SPEED_SCALE = 0.5f;
    public static final float BACKGROUND_1_MIN_HEIGHT = 100;
    public static final float BACKGROUND_1_MAX_HEIGHT = 250;
    public static final float BACKGROUND_1_MIN_WIDTH = 60;
    public static final float BACKGROUND_1_MAX_WIDTH = 120;
    public static final float BACKGROUND_0_SPEED_SCALE = 0.25f;
    public static final float BACKGROUND_0_MIN_HEIGHT = 200;
    public static final float BACKGROUND_0_MAX_HEIGHT = 400;
    public static final float BACKGROUND_0_MIN_WIDTH = 30;
    public static final float BACKGROUND_0_MAX_WIDTH = 60;
    public static final int MAX_BACKGROUND_RECTS = 20;

    public static LevelGeneratorParams getDefaultLevelGeneratorParams(){
        LevelGeneratorParams lgp = new LevelGeneratorParams();
        lgp.levelTotalMeters = -1;
        lgp.levelCoinsToWin = -1;
        lgp.levelInitialSpeed = 70;
        lgp.maxNumberOfSimultaneousObstacles = 4;
        lgp.obstacleExplosiveCreationBoost = 1.5f;
        lgp.obstacleExplosiveCreationChance = 0.000000001;
        lgp.obstacleLinearCreationPerSecond = 0.8f;
        lgp.coinLinearCreationPerSecond = 1.0f;
        lgp.augmentSpeedEachXPixels = 100000;
        lgp.augmentSpeedByYEachXPixels = 10;
        lgp.maxNumberOfSimultaneousCoins = 20;
        return lgp;
    }

}
