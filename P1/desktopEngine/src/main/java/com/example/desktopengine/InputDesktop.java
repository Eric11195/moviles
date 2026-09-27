package com.example.desktopengine;

import com.example.engine.InputInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;

import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class InputDesktop implements InputInterface {
    ArrayList<TouchEvent> eventList;
    public InputDesktop(){
        eventList = new ArrayList<>();
    }

    public void registerCanvas(Canvas canvas){
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                var temp = new TouchEvent();
                temp.x = mouseEvent.getX();
                temp.y = mouseEvent.getY();
                temp.type = TouchEventType.DOWN;
                temp.id = mouseEvent.getID();
                eventList.add(temp);
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent){
                super.mouseReleased(mouseEvent);
                var temp = new TouchEvent();
                temp.x = mouseEvent.getX();
                temp.y = mouseEvent.getY();
                temp.type = TouchEventType.UP;
                temp.id = mouseEvent.getID();
                eventList.add(temp);
            }
        });
    }
    @Override
    public List<TouchEvent> getTouchEvents(){
        ArrayList<TouchEvent> temp = new ArrayList<>();
        eventList.forEach( event -> temp.add(event.clone()));
        eventList = new ArrayList<>();
        return temp;
    }
}
