package com.example.engine;

public class TouchEvent {
    public float x = 0;
    public float y = 0;
    public TouchEventType type = TouchEventType.UNKNOWN;
    public int id = 0;
    public TouchEvent clone(){
        var temp = new TouchEvent();
        temp.x = this.x;
        temp.y = this.y;
        temp.type = this.type;
        temp.id = this.id;
        return temp;
    }
}
