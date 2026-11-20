package com.example.androidengine;

import static android.view.MotionEvent.*;

import android.view.MotionEvent;
import android.view.View;

import com.example.engine.InputBase;
import com.example.engine.TouchEventType;
import com.example.utils.Vec2;

/**
 * Android implementation of {@link InputBase} registering View OnTouchListener.
 */
public class InputAndroid extends InputBase {

    /**
     * Registers an Android View to capture MotionEvent touches into the InputBase queue.
     *
     * @param _view Android View instance
     */
    public void registerView(View _view) {
        _view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        var temp = eventPool.fetchItem();
                        temp.pos = new Vec2(event.getX(), event.getY());
                        switch (event.getAction()) {
                            case ACTION_DOWN:
                            case ACTION_POINTER_DOWN:
                                temp.type = TouchEventType.DOWN;
                                break;
                            case ACTION_UP:
                            case ACTION_POINTER_UP:
                                temp.type = TouchEventType.UP;
                                break;
                            case ACTION_MOVE:
                                temp.type = TouchEventType.MOVE;
                                break;
                        }
                        temp.id = event.getActionIndex();

                        synchronized (this) {
                            eventList.add(temp);
                        }
                        return true;
                    }
                }
        );
    }
}
