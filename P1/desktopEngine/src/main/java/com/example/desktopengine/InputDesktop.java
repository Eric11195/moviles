package com.example.desktopengine;

import com.example.engine.InputBase;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;
import com.example.utils.Vec2;

import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InputDesktop extends InputBase {
    public void registerCanvas(Canvas canvas){
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                var temp = new TouchEvent();
                temp.pos = new Vec2(mouseEvent.getX(), mouseEvent.getY());
                temp.type = TouchEventType.DOWN;
                temp.id = mouseEvent.getID();

                synchronized (this) {
                    eventList.add(temp);
                }
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent){
                super.mouseReleased(mouseEvent);
                var temp = new TouchEvent();
                temp.pos = new Vec2(mouseEvent.getX(), mouseEvent.getY());
                temp.type = TouchEventType.UP;
                temp.id = mouseEvent.getID();

                synchronized (this) {
                    eventList.add(temp);
                }
            }
        });
    }

}
