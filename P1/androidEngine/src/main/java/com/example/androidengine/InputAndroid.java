package com.example.androidengine;



import static android.view.MotionEvent.*;

import android.view.MotionEvent;
import android.view.View;

import com.example.engine.InputInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;

import java.util.ArrayList;
import java.util.List;

public class InputAndroid implements InputInterface {
    private List<TouchEvent> eventList;

    public InputAndroid(){
        eventList = new ArrayList<>();
    }

    public void registerView (View _view){
        _view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        var temp = new TouchEvent();
                        temp.x = event.getX();
                        temp.y = event.getY();
                        switch (event.getAction()){
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
                        eventList.add(temp);
                        return true;
                    }
                }
        );
    }

    @Override
    public List<TouchEvent> getTouchEvents(){
        ArrayList<TouchEvent> temp = new ArrayList<>();
        eventList.forEach( event -> temp.add(event.clone()));
        eventList = new ArrayList<>();
        return temp;
    }
}
