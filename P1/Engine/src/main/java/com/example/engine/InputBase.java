package com.example.engine;

import com.example.utils.Pool;
import com.example.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

public abstract class InputBase {
    final static int CAPACITY = 30;
    final TouchEventType state_this_frame = new TouchEventType(TouchEventType.None);
    private boolean pressed = false;
    public interface screenToGamePos{
        public Vec2 toGamePos(Vec2 screenPos);
    }
    protected final Pool<TouchEvent> eventPool = new Pool<>(CAPACITY, new TouchEvent());
    protected final List<TouchEvent> eventList = new ArrayList<>();;
    private final List<TouchEvent> lastFrameEvents = new ArrayList<>();
    public final List<TouchEvent> getTouchEvents(){
        return lastFrameEvents;
    }
    synchronized public void update(screenToGamePos posTranslator) {
        lastFrameEvents.forEach(eventPool::releaseItem);
        lastFrameEvents.clear();
        lastFrameEvents.addAll(eventList);
        state_this_frame.setNone();
        lastFrameEvents.forEach((tE)->{
            tE.pos = posTranslator.toGamePos(tE.pos);
            state_this_frame.or(tE.type);
        });
        pressed = (pressed&&!upThisFrame() || (downThisFrame()));
        eventList.clear();
    }
    public boolean pressed(){
        return pressed;
    }
    public boolean downThisFrame(){
        return state_this_frame.contains(TouchEventType.DOWN);
    }
    public boolean upThisFrame(){
        return state_this_frame.contains(TouchEventType.UP);
    }
    public boolean draggedThisFrame(){
        return state_this_frame.contains(TouchEventType.MOVE);
    }
}
