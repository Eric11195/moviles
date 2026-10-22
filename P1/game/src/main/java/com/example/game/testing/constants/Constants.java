package com.example.game.testing.constants;
import com.example.utils.Vec2;


public class Constants {
    public static final Vec2 LOGIC_WINDOW_SIZE = new Vec2(600,400);

    public static final int PLAYER_X_POS =(int) LOGIC_WINDOW_SIZE.x/5;
    public static final Vec2 PLAYER_DIMENSIONS= new Vec2(LOGIC_WINDOW_SIZE.x/12, LOGIC_WINDOW_SIZE.y/10);
    //PLAYER
    public static final float PLAYER_STARTING_Y_POS = LOGIC_WINDOW_SIZE.y-PLAYER_DIMENSIONS.y;
    public static final Vec2 PLAYER_MARGINS = new Vec2(PLAYER_DIMENSIONS.x/2, PLAYER_DIMENSIONS.y/2);
    //Vertical upwards
    public static final float MAX_PLAYER_UPWARDS_SPEED = 120;
    public static final float Y_PLAYER_ACCEL = 400;
    public static final float GRAVITY_FORCE = 200;
    public static final float MAX_PLAYER_FALL_SPEED = 160;

    //UI
    public static final Vec2 DEFAULT_BUTTON_SIZE = new Vec2(LOGIC_WINDOW_SIZE.x*0.25f,LOGIC_WINDOW_SIZE.y*0.15f);

    //OBSTACLES
    public static final int DANGER_MATRIX_PRECALCULATED_SPACE = (int)(LOGIC_WINDOW_SIZE.x * 0.5f);
        // SCENE GENERATOR
        public static final int DANGER_MATRIX_PX_TILE_SIZE = 2;

        // ZAPPERS
        public static final float MIN_ZAPPER_WIDTH = 60;
        public static final float MAX_ZAPPER_WIDTH = DANGER_MATRIX_PRECALCULATED_SPACE *0.6f;
        public static final float ZAPPER_DELTA_WIDTH = MAX_ZAPPER_WIDTH - MIN_ZAPPER_WIDTH;
        public static final float MIN_ZAPPER_HEIGHT = 10;
        public static final float ZAPPER_HEAD_RADIUS = 8;
        public static final float ZAPPER_LINE_WIDTH = 5;
        public static final int ZAPPER_MAX_COLLIDERS = 10;
        public static final float MAX_ZAPPER_LENGTH = 100;

        // MISILES
        public static final float MISSILE_MOVE_SPEED = LOGIC_WINDOW_SIZE.x*2;
        public static final float MISSILE_RADIUS = 30;

        // LASER
        public static final float LASER_HEIGHT = LOGIC_WINDOW_SIZE.y/5.0f;

        // COIN
        public static final int MAX_COINS = 5;

    /**
     * How long until an obstacle with warning appears from the first time it's hinted on screen
     */
    public static final float WARNING_TIME = 2;
    public static final float WARNING_RADIUS = 30;

    public static final float LASER_WIDTH = LOGIC_WINDOW_SIZE.x;
    public static final float LASER_ANIM_TIME = 0.5f;
    public  static final int LASER_ANIM_NUM = 4;
    public static final float LASER_ATTACK_DUR= 1.5f;

}
