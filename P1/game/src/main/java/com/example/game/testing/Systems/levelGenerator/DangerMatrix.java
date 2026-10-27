package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PRECALCULATED_SPACE;
import static com.example.game.testing.constants.Constants.GRAVITY_FORCE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_FALL_SPEED;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_UPWARDS_SPEED;
import static com.example.game.testing.constants.Constants.PLAYER_MARGINS;
import static com.example.game.testing.constants.Constants.Y_PLAYER_ACCEL;

import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.utils.Vec2;

public class DangerMatrix extends BoolMatrix{
    int zonesPxSize;
    public DangerMatrix(int zonesPxSize){
        super((int)Math.ceil((LOGIC_WINDOW_SIZE.x+DANGER_MATRIX_PRECALCULATED_SPACE)/zonesPxSize), (int)Math.ceil(LOGIC_WINDOW_SIZE.y/zonesPxSize));
        this.zonesPxSize = zonesPxSize;
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
     * @return true if that line is free on the danger Matrix, false otherwise
     */
    public boolean checkLine(Vec2 startPt, Vec2 endPt){
        Vec2 delta = endPt.sub(startPt);
        int repetitions = (int)Math.ceil(delta.length() / (zonesPxSize/5.0f));
        Vec2 pt = startPt;
        for(int i = 0; i < repetitions; ++i){
            if(get(getConvertedPos(pt))) return false;
            pt = pt.add(delta);
        }
        return true;
    }

    /**
     * @param x0y0 the left up point of the rect
     * @param size width and height in px
     * @return true if the given rect is free on the danger matrix
     */
    public boolean checkRect(Vec2 x0y0, Vec2 size){
        Vec2 startGrid = getConvertedPos(x0y0);
        Vec2 endGrid = getConvertedPos(x0y0.add(size));

        for(int x = (int)startGrid.x; x <= endGrid.x; ++x){
            for(int y = (int)startGrid.y; y <= endGrid.y; ++y){
                if(get(x, y)) return false;
            }
        }
        return true;
    }

    public void drawSquare(Vec2 x0y0, Vec2 size){
        set(getConvertedPos(x0y0),getConvertedPos(x0y0.add(size)), true);
    }
    /**
     * Converts the vector so it cannot fail in danger matrix
     * This modifies the given vector
     */
    private Vec2 sanitize(Vec2 p) {
        p.x = Math.max(0, Math.min(getWidth() - 1, p.x));
        p.y = Math.max(0, Math.min(getHeight() - 1, p.y));
        return p;
    }
    public Vec2 getConvertedPos(Vec2 pos){
        return sanitize(new Vec2(
            pos.x/zonesPxSize,
            pos.y/zonesPxSize
        ));
    }
    public Vec2 getConvertedPos(int x, int y){
        return sanitize(
            new Vec2(
                x/(float)zonesPxSize,
                y/(float)zonesPxSize
            )
        );
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

    public boolean ensureTraversability(
        mcd_Entity playerEnt,
        float dt
    ){
        TransformComponent playerTr =
            (TransformComponent) playerEnt.getComponent(ComponentId.TRANSFORM_COMPONENT);
        VelocityComponent playerVel =
            (VelocityComponent) playerEnt.getComponent(ComponentId.VELOCITY_COMPONENT);
        if(playerTr == null || playerVel == null)
            throw new RuntimeException(
                "Player had no transform or velocity components. That should never happen." +
                "Ensure it has both of them before calling this function"
            );
        return ensureTraversability(
            playerTr.size.x+PLAYER_MARGINS.x, playerTr.size.y+PLAYER_MARGINS.y,
            LevelSpeedSystem.getInstance().getSpeedMult()
        );
    }

    private boolean ensureTraversability(
            float playerWidth,
            float playerHeight,
            float horizontalSpeed
    ) {
        if (horizontalSpeed <= 0 || zonesPxSize <= 0 || playerWidth <= 0 || playerHeight <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        final int yCount = getHeight();
        final int xCount = getWidth();

        // ONLY start checking from where new obstacles are generated (WINDOW_WIDTH)
        int startX = (int) (LOGIC_WINDOW_SIZE.x / zonesPxSize);
        startX = Math.max(0, Math.min(xCount - 1, startX));

        // Cap the right-most check so the player bounding box doesn't go out of bounds
        final int endX = xCount - 1 - (int) Math.floor((playerWidth - 0.001f) / zonesPxSize);

        if (endX <= startX) return true;

        final float dt = zonesPxSize / horizontalSpeed;

        // Maximum cells the player can move up or down per horizontal column
        final int cellsUp = (int) Math.ceil((MAX_PLAYER_UPWARDS_SPEED * dt) / zonesPxSize);
        final int cellsDown = (int) Math.ceil((MAX_PLAYER_FALL_SPEED * dt) / zonesPxSize);

        boolean[] currentReachable = new boolean[yCount];
        boolean[] nextReachable = new boolean[yCount];

        // 1. INITIALIZE: Assume any free space at the start of our check is a valid entry point,
        // since the path up to this column was already validated in previous frames.
        boolean entryFound = false;
        for (int y = 0; y < yCount; ++y) {
            if (isCharacterFree(startX * (float) zonesPxSize, y * (float) zonesPxSize, playerWidth, playerHeight)) {
                currentReachable[y] = true;
                entryFound = true;
            }
        }

        if (!entryFound) return false;

        // 2. FORWARD CHECK: Only iterate through the newly generated columns on the far right
        for (int x = startX + 1; x <= endX; ++x) {
            boolean anyReachable = false;
            java.util.Arrays.fill(nextReachable, false);

            for (int y = 0; y < yCount; ++y) {
                if (!currentReachable[y]) continue;

                // Create a reachable window based on max speeds
                int minY = Math.max(0, y - cellsUp);
                int maxY = Math.min(yCount - 1, y + cellsDown);

                for (int nextY = minY; nextY <= maxY; ++nextY) {
                    if (nextReachable[nextY]) continue; // Skip if already marked valid

                    // If the bounding box fits in this reachable cell, mark it
                    if (isCharacterFree(x * (float) zonesPxSize, nextY * (float) zonesPxSize, playerWidth, playerHeight)) {
                        nextReachable[nextY] = true;
                        anyReachable = true;
                    }
                }
            }

            // If a newly generated column creates a solid wall, reject the obstacle
            if (!anyReachable) return false;

            boolean[] temp = currentReachable;
            currentReachable = nextReachable;
            nextReachable = temp;
        }

        return true;
    }

    /**
     * Checks every grid cell touched by the character's bounding box.
     */
    private boolean isCharacterFree(
            float x, float y, float playerWidth, float playerHeight
    ) {
        int minX = (int) Math.floor(x / zonesPxSize);
        int minY = (int) Math.floor(y / zonesPxSize);

        // Subtract a tiny epsilon so an edge exactly on a cell boundary
        // does not count as overlapping the next cell.
        int maxX = (int) Math.floor(
                (x + playerWidth - 0.001f) / zonesPxSize
        );
        int maxY = (int) Math.floor(
                (y + playerHeight - 0.001f) / zonesPxSize
        );

        for (int cx = minX; cx <= maxX; ++cx) {
            for (int cy = minY; cy <= maxY; ++cy) {
                if (cx < 0 || cx >= getWidth()
                        || cy < 0 || cy >= getHeight()) {
                    return false;
                }

                if (get(cx, cy)) return false;
            }
        }

        return true;
    }

}
