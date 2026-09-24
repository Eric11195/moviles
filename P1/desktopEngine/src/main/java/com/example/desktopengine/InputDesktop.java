package com.example.desktopengine;

import com.example.engine.InputInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class InputDesktop implements InputInterface {
    ArrayList<TouchEvent> eventList;
    public InputDesktop(){
        eventList = new ArrayList<>();
    }

    public void registerPanel(JPanel panel){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                var temp = new TouchEvent();
                temp.x = mouseEvent.getX();
                temp.y = mouseEvent.getY();
                temp.type = TouchEventType.DOWN;
                temp.id = mouseEvent.getID();
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent){
                super.mouseReleased(mouseEvent);
                var temp = new TouchEvent();
                temp.x = mouseEvent.getX();
                temp.y = mouseEvent.getY();
                temp.type = TouchEventType.UP;
                temp.id = mouseEvent.getID();
            }
        });
    }
    @Override
    public List<TouchEvent> getTouchEvents(){
        ArrayList<TouchEvent> temp = new ArrayList<>();
        eventList.forEach( event -> temp.add(event.clone()));
        return temp;
    }
}
