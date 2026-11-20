package com.example.engine;

import com.example.utils.Pool;
import com.example.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for handling input events, coordinate translation, and touch state queries across frames.
 */
public abstract class InputBase {

    /** Maximum capacity of touch event pool. */
    final static int CAPACITY = 30;

    /** Accumulated touch event types occurring in the current frame. */
    final TouchEventType state_this_frame = new TouchEventType(TouchEventType.None);

    /** Flag tracking whether a pointer/button is held down. */
    private boolean pressed = false;

    /**
     * Interface for mapping screen coordinates to logical game coordinate space.
     */
    public interface screenToGamePos {
        /**
         * Translates a point in screen space to logical game space.
         *
         * @param screenPos position in screen coordinates
         * @return translated position in game coordinates
         */
        public Vec2 toGamePos(Vec2 screenPos);
    }

    /** Pool for reusing TouchEvent instances. */
    protected final Pool<TouchEvent> eventPool = new Pool<>(CAPACITY, new TouchEvent());

    /** Pending touch events accumulated for the upcoming frame. */
    protected final List<TouchEvent> eventList = new ArrayList<>();

    /** Active touch events available for processing during the current frame. */
    private final List<TouchEvent> lastFrameEvents = new ArrayList<>();

    /**
     * Retrieves the list of touch events that occurred during the current frame.
     *
     * @return list of TouchEvent instances
     */
    public final List<TouchEvent> getTouchEvents() {
        return lastFrameEvents;
    }

    /**
     * Updates frame input state, releasing old events and translating positions using the translator.
     *
     * @param posTranslator coordinate mapping function
     */
    synchronized public void update(screenToGamePos posTranslator) {
        lastFrameEvents.forEach(eventPool::releaseItem);
        lastFrameEvents.clear();
        lastFrameEvents.addAll(eventList);
        state_this_frame.setNone();
        lastFrameEvents.forEach((tE) -> {
            tE.pos = posTranslator.toGamePos(tE.pos);
            state_this_frame.or(tE.type);
        });
        pressed = (pressed && !upThisFrame() || (downThisFrame()));
        eventList.clear();
    }

    /**
     * Checks if a pointer/button is currently held pressed.
     *
     * @return true if pressed, false otherwise
     */
    public boolean pressed() {
        return pressed;
    }

    /**
     * Checks if a DOWN touch event occurred in this frame.
     *
     * @return true if DOWN occurred, false otherwise
     */
    public boolean downThisFrame() {
        return state_this_frame.contains(TouchEventType.DOWN);
    }

    /**
     * Checks if an UP touch event occurred in this frame.
     *
     * @return true if UP occurred, false otherwise
     */
    public boolean upThisFrame() {
        return state_this_frame.contains(TouchEventType.UP);
    }

    /**
     * Checks if a MOVE (drag) touch event occurred in this frame.
     *
     * @return true if MOVE occurred, false otherwise
     */
    public boolean draggedThisFrame() {
        return state_this_frame.contains(TouchEventType.MOVE);
    }
}
