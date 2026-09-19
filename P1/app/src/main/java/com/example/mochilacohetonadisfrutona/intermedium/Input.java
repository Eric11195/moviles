package com.example.mochilacohetonadisfrutona.intermedium;



import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Input {
    public static class TouchEvent{
        public TouchEvent(int _x, int _y, View _target ){
            x = _x;
            y = _y;
            target = _target;
        }
        int x;
        int y;
        View target;
        public TouchEvent clone(){
            return new TouchEvent(this.x,this.y,this.target);
        }
    }

    private List<TouchEvent> eventList;

    public Input(){
        eventList = new ArrayList<>();
    }

    public void onTap(){
//        eventList.add(new TouchEvent());
    }

    public List<TouchEvent> GetTouchEvents(){
        ArrayList<TouchEvent> temp = new ArrayList<>();
        eventList.forEach( event -> temp.add(event.clone()));
        return temp;
    }
}
