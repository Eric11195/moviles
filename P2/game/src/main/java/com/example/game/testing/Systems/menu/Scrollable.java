package com.example.game.testing.Systems.menu;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.TransformComponent;
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
    Scrollable(int rows, int cols, List<TransformComponent> trList){
        //Calculate the number of vertical elements that fit the screen
        this.totalRows = Math.max(rows, 1+(trList.size()/cols));
        this.rows = rows;
        this.cols = cols;
        this.allMyObjectsTransform = trList;
        this.barProgress = 0;
        this.barSize = Math.min(1, this.rows / ((float)this.totalRows));
    }
    final List<TransformComponent> allMyObjectsTransform;
    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        //Todo: Move each object transform so that they fit on screen based on barProgress
        int i = 0;
        int row = 0;
        while( i < this.allMyObjectsTransform.size()){
            //Calculate Y center
            //TODO: add the scrolled distance to this y
            float y = LOGIC_WINDOW_SIZE.y* (row / (this.rows+2.0f));
            int limit = i+this.cols;
            for(int j = i; j < this.allMyObjectsTransform.size() && j < limit; ++j){
                //Calculate X center
                float x = LOGIC_WINDOW_SIZE.x * ((limit-j)/ (this.cols+2.0f));
                TransformComponent tr = this.allMyObjectsTransform.get(j);
                tr.pos.x = x - tr.size.x/2.0f;
                tr.pos.y = y - tr.size.y/2.0f;
            }
            i += this.cols;
            ++row;
        }
        //Todo: detect bar mouse Movement
    }

    @Override
    public void render(GraphicsInterface g) {
        //render bar
    }
}
