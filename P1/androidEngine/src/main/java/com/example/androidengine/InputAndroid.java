package com.example.androidengine;



import static android.view.MotionEvent.*;

import android.view.MotionEvent;
import android.view.View;

import com.example.engine.InputBase;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;
import com.example.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

public class InputAndroid extends InputBase {
    public void registerView (View _view){
        _view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        var temp = new TouchEvent();
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
