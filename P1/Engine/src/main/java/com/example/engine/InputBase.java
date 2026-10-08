package com.example.engine;

import com.example.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

public abstract class InputBase {
    public interface screenToGamePos{
        public Vec2 toGamePos(Vec2 screenPos);
    }

    protected final List<TouchEvent> eventList = new ArrayList<>();;
    private final List<TouchEvent> lastFrameEvents = new ArrayList<>();
    public final List<TouchEvent> getTouchEvents(){
        return lastFrameEvents;
    }
    public void update(screenToGamePos posTranslator) {
        lastFrameEvents.clear();
        lastFrameEvents.addAll(eventList);
        eventList.clear();
    }
}
