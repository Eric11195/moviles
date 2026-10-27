package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Pool;
import com.example.utils.Vec2;

public class BackgroundComponent implements mcd_Component {
    final float xOffset = -1;
    final float yOffset = 1;
    Vec2 minDims;
    Vec2 maxDims;
    float speedScale;
    ColorEng color;
    Pool<DrawRectangleComponent.Rectangle> rectanglePool = new Pool<>(Constants.MAX_BACKGROUND_RECTS,
            new DrawRectangleComponent.Rectangle(new Vec2(0,0),new Vec2(0,0)));
    public BackgroundComponent(Vec2 _minDims, Vec2 _maxDims, float _speedScale, ColorEng _color){
        minDims = _minDims;
        maxDims = _maxDims;
        speedScale = _speedScale;
        color = _color;
    }
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        DrawRectangleComponent rectangleComponent = (DrawRectangleComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);

        fillUpRects(rectangleComponent);

        float spd = LevelSpeedSystem.getInstance().getSpeedMult();

        for (int i = 0; i < rectangleComponent.rectanglesToDraw.size(); i++){
            DrawRectangleComponent.Rectangle r = rectangleComponent.rectanglesToDraw.get(i);

            r.myPos.x -= spd * dt * speedScale;

            if (r.myPos.x + r.mySize.x < 0) {
                DrawRectangleComponent.Rectangle removeRect = rectangleComponent.rectanglesToDraw.remove(i);
                rectanglePool.releaseItem(removeRect);
                i--;
            }
        }

        rectangleComponent.color = color;

    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
    }
    void fillUpRects(DrawRectangleComponent rectangleComponent){
        Vec2 pos;
        Vec2 size;
        if (rectangleComponent.rectanglesToDraw.isEmpty()){
            float h = (float)Math.random() * (maxDims.y - minDims.y) + minDims.y;
            pos = new Vec2(xOffset, Constants.LOGIC_WINDOW_SIZE.y-h+yOffset);
            float w = (float)Math.random() * (maxDims.x - minDims.x) + minDims.x;
            size = new Vec2(w,h);

            DrawRectangleComponent.Rectangle addRect = rectanglePool.fetchItem();
            addRect.myPos = pos;
            addRect.mySize = size;

            rectangleComponent.rectanglesToDraw.add(addRect);
        }

        DrawRectangleComponent.Rectangle rect = rectangleComponent.rectanglesToDraw.get(rectangleComponent.rectanglesToDraw.size()-1);
        while(rect.myPos.x + rect.mySize.x < Constants.LOGIC_WINDOW_SIZE.x){
            float h = (float)Math.random() * (maxDims.y - minDims.y) + minDims.y;
            pos = new Vec2(rect.myPos.x+rect.mySize.x+xOffset, Constants.LOGIC_WINDOW_SIZE.y-h+yOffset);
            float w = (float)Math.random() * (maxDims.x - minDims.x) + minDims.x;
            size = new Vec2(w,h);

            DrawRectangleComponent.Rectangle addRect = rectanglePool.fetchItem();
            addRect.myPos = pos;
            addRect.mySize = size;

            rectangleComponent.rectanglesToDraw.add(addRect);
            rect = rectangleComponent.rectanglesToDraw.get(rectangleComponent.rectanglesToDraw.size()-1);
        }
    }
}
