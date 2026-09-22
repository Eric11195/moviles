package com.example.androidengine;



import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class InputAndroid {
    public static class TouchEvent{
        public TouchEvent(View _target, MotionEvent _event, long _eventTime){
            target = _target;
            event = _event;
            eventTime = _eventTime;
        }
        View target;
        MotionEvent event;
        long eventTime;
        public TouchEvent clone(){
            return new TouchEvent(this.target,this.event,this.eventTime);
        }
    }

    private List<TouchEvent> eventList;

    public InputAndroid(){
        eventList = new ArrayList<>();
    }

    public void registerView (View _view){
        _view.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        eventList.add(new TouchEvent(v, event,System.currentTimeMillis()));
                        return true;
                    }
                }
        );
    }

    public List<TouchEvent> GetTouchEvents(){
        ArrayList<TouchEvent> temp = new ArrayList<>();
        eventList.forEach( event -> temp.add(event.clone()));
        return temp;
    }
}
