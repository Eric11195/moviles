package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class InputBase {
    protected final List<TouchEvent> eventList = new ArrayList<>();;
    private final List<TouchEvent> lastFrameEvents = new ArrayList<>();
    public final List<TouchEvent> getTouchEvents(){
        return lastFrameEvents;
    }
    public void update() {
        lastFrameEvents.clear();
        for (TouchEvent evt : new ArrayList<>(eventList)) {
            lastFrameEvents.add(evt.clone());
        }
        eventList.clear();
    }
}
