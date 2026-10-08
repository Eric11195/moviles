package com.example.engine;

import com.example.utils.Pool;
import com.example.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

public abstract class InputBase {
    final static int CAPACITY = 30;
    public interface screenToGamePos{
        public Vec2 toGamePos(Vec2 screenPos);
    }
    Pool<TouchEvent> eventPool = new Pool<>(CAPACITY);
    protected final List<TouchEvent> eventList = new ArrayList<>();;
    private final List<TouchEvent> lastFrameEvents = new ArrayList<>();
    public final List<TouchEvent> getTouchEvents(){
        return lastFrameEvents;
    }
    synchronized public void update(screenToGamePos posTranslator) {
        lastFrameEvents.clear();
        lastFrameEvents.addAll(eventList);
        eventList.clear();
    }


}
