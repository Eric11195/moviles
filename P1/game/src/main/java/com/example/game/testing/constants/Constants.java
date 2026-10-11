package com.example.game.testing.constants;
import com.example.utils.Vec2;


public class Constants {
    public static final Vec2 LOGIC_WINDOW_SIZE = new Vec2(600,400);
    public static final int PLAYER_X_POS =(int) LOGIC_WINDOW_SIZE.x/3;
    public static final Vec2 LASER_DIMENSIONS = new Vec2(LOGIC_WINDOW_SIZE.x, LOGIC_WINDOW_SIZE.y/2);
    public static final Vec2 PLAYER_DIMENSIONS= new Vec2(LOGIC_WINDOW_SIZE.x/12, LOGIC_WINDOW_SIZE.y/10);
    public static final Vec2 PLAYER_MARGINS = new Vec2(PLAYER_DIMENSIONS.x/2, PLAYER_DIMENSIONS.y/2);
    //Vertical upwards
    public static final float MAX_PLAYER_SPEED = 25;
    public static final float Y_PLAYER_ACCEL = 10;
    //Vertical downwards
    public static final float GRAVITY_FORCE = 20;
    public static final float MAX_PLAYER_FALL_SPEED = 30;
    public static final float MIN_ZAPPER_WIDTH = 60;
    public static final float MAX_ZAPPER_WIDTH = 160;
    public static final float ZAPPER_DELTA_WIDTH = MAX_ZAPPER_WIDTH - MIN_ZAPPER_WIDTH;

}
