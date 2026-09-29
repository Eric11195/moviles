package com.example.desktopengine;

import com.example.engine.InputBase;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;

import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InputDesktop extends InputBase {
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

}
