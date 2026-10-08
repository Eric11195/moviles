package com.example.engine;

import com.example.utils.Vec2;

public class TouchEvent {
    public Vec2 pos = new Vec2(0,0);
    public TouchEventType type = TouchEventType.UNKNOWN;
    public int id = 0;
}
