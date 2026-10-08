package com.example.engine;

import com.example.utils.PoolObject;
import com.example.utils.Vec2;
import com.example.utils.mcd_Cloneable;

public class TouchEvent extends PoolObject<TouchEvent> {
    public Vec2 pos = new Vec2(0,0);
    public TouchEventType type = TouchEventType.UNKNOWN;
    public int id = 0;

    @Override
    public TouchEvent clone() {
        TouchEvent temp = new TouchEvent();
        temp.pos = pos.clone();
        temp.type = type;
        temp.id = id;
        return temp;
    }
}
