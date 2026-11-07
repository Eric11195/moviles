package com.example.game.testing.Systems.menu;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.mcd_System;
import com.example.utils.Vec2;

import java.util.List;

public class Scrollable implements mcd_System {
    private int rows;
    private int totalRows;
    private int cols;
    float barProgress;
    float barSize;
    Vec2 startPos;
    Vec2 size;
    //Probably needs limits in size and position in constructor for it to recalculate correctly
    public Scrollable(Vec2 startPos, Vec2 size, int rows, int cols, List<mcd_Entity> trList){
        //Calculate the number of vertical elements that fit the screen
        this.startPos = startPos;
        this.size = size;
        this.totalRows = Math.max(rows, (trList.size() + cols - 1) / cols);
        this.rows = rows;
        this.cols = cols;
        this.allMyObjects = trList;
        this.barProgress = 0;
        this.barSize = Math.min(1, this.rows / ((float)this.totalRows));
    }
    final List<mcd_Entity> allMyObjects;
    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        //Todo: Move each object transform so that they fit on screen based on barProgress
        int i = 0;
        int row = 0;
        float scrollOffset = barProgress * (totalRows - rows);
        while( i < this.allMyObjects.size()){
            //Calculate Y
            //TODO: add the scrolled distance to this y
            float visibleRow = row - scrollOffset;
            float y = startPos.y + size.y * visibleRow /(float)rows;
            int limit = i+this.cols;
            for(int j = i; j < this.allMyObjects.size() && j < limit; ++j){
                //Calculate X
                int col = j - i;
                float x = startPos.x + size.x * col / (float)cols;
                mcd_Entity ent = this.allMyObjects.get(j);
                TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
                tr.pos.x = x;
                tr.pos.y = y;
            }
            i += this.cols;
            ++row;
        }
        //Todo: detect bar mouse Movement

        //Update all inner objects
        allMyObjects.forEach(ent->{
            ent.update(t,dt);
        });
    }

    @Override
    public void render(GraphicsInterface g) {
        //todo: render bar
        //render all inner objects
        allMyObjects.forEach(ent -> {
            ent.render(g);
        });
    }
    public void setActive(boolean value){
        allMyObjects.forEach((ent)->{
            ent.setActive(value);
        });
    }
}
