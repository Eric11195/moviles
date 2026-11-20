package com.example.engine;

import com.example.utils.PoolObject;
import com.example.utils.Vec2;

/**
 * Class representing a single touch or pointer input event.
 */
public class TouchEvent extends PoolObject<TouchEvent> {

    /** Position coordinate of the touch event. */
    public Vec2 pos = new Vec2(0, 0);

    /** Type of touch event (DOWN, UP, MOVE, etc.). */
    public TouchEventType type = TouchEventType.UNKNOWN;

    /** Pointer/finger ID. */
    public int id = 0;

    /**
     * Creates a copy of this touch event.
     *
     * @return cloned TouchEvent instance
     */
    @Override
    public TouchEvent clone() {
        TouchEvent temp = new TouchEvent();
        temp.pos = pos.clone();
        temp.type = type;
        temp.id = id;
        return temp;
    }
}
