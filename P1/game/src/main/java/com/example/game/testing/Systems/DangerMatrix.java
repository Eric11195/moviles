package com.example.game.testing.Systems;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PRECALCULATED_SPACE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.utils.Vec2;

public class DangerMatrix extends BoolMatrix{
    int zonesPxSize;
    int pointCheckingDensity;
    public DangerMatrix(int zonesPxSize){
        super((int)Math.ceil((LOGIC_WINDOW_SIZE.x+DANGER_MATRIX_PRECALCULATED_SPACE)/zonesPxSize), (int)Math.ceil(LOGIC_WINDOW_SIZE.y/zonesPxSize));
        this.zonesPxSize = zonesPxSize;
        this.pointCheckingDensity = (int)Math.floor(Math.sqrt(zonesPxSize));
    }
    public void reset(){
        clear();
    }
    public void drawLine(Vec2 startPt, Vec2 endPt){
        Vec2 delta = endPt.sub(startPt);
        int repetitions = (int)Math.ceil(delta.length() / zonesPxSize);
        Vec2 pt = startPt;
        for(int i = 0; i < repetitions; ++i){
            set(getConvertedPos(pt), true);
            pt = pt.add(delta);
        }
    }

    /**
     *
     * @return true if that line is free on the danger Matrix, false otherwise
     */
    public boolean checkLine(Vec2 startPt, Vec2 endPt){
        Vec2 delta = endPt.sub(startPt);
        int repetitions = (int)Math.ceil(delta.length() / zonesPxSize);
        Vec2 pt = startPt;
        for(int i = 0; i < repetitions; ++i){
            if(get(getConvertedPos(pt))) return false;
            pt = pt.add(delta);
        }
        return true;
    }
    public void drawSquare(Vec2 x0y0, Vec2 size){
        set(getConvertedPos(x0y0),getConvertedPos(size), true);
    }
    /**
     * Converts the vector so it cannot fail in danger matrix
     * This modifies the given vector
     */
    private Vec2 sanitize(Vec2 p){
        p.x = Math.min(LOGIC_WINDOW_SIZE.x+DANGER_MATRIX_PRECALCULATED_SPACE, Math.max(0, p.x));
        p.y = Math.min(LOGIC_WINDOW_SIZE.y, Math.max(0,p.y));
        return p;
    }
    public Vec2 getConvertedPos(Vec2 pos){
        return sanitize(new Vec2(
            pos.x/zonesPxSize,
            pos.y/zonesPxSize
        ));
    }

    /**
     * @param xPos
     * @return A vec2 with (y1,y2) y1<=y2 Where y1-y2 is the first encountered vertical free range
     */
    public Vec2 getFreeHeightRange(float xPos){
        Vec2 start = new Vec2(xPos,0);
        while(start.y/zonesPxSize < getHeight() && get(getConvertedPos(start))){
            start.y += zonesPxSize;
        }
        Vec2 end = new Vec2(xPos,start.y);
        while(end.y/zonesPxSize < getHeight() && !get(getConvertedPos(end))){
            end.y += zonesPxSize;
        }
        return new Vec2(start.y, end.y);
    }
}
