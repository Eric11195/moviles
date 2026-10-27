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
        int repetitions = Math.max(1, (int)Math.ceil(delta.length() / zonesPxSize));
        // Calculate the small incremental step instead of the full length
        Vec2 step = delta.mul(1.0f / repetitions);

        Vec2 pt = startPt;
        for(int i = 0; i <= repetitions; ++i){
            set(getConvertedPos(pt), true);
            pt = pt.add(step);
        }
    }

    /**
     * @return true if that line is free on the danger Matrix, false otherwise
     */
    public boolean checkLine(Vec2 startPt, Vec2 endPt){
        Vec2 delta = endPt.sub(startPt);
        int repetitions = Math.max(1, (int)Math.ceil(delta.length() / (zonesPxSize/5.0f)));
        // Calculate the small incremental step
        Vec2 step = delta.mul(1.0f / repetitions);

        Vec2 pt = startPt;
        for(int i = 0; i <= repetitions; ++i){
            if(get(getConvertedPos(pt))) return false;
            pt = pt.add(step);
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
            playerTr.pos.x, playerTr.pos.y,
            playerTr.size.x+PLAYER_MARGINS.x, playerTr.size.y+PLAYER_MARGINS.y,
            LevelSpeedSystem.getInstance().getSpeedMult()
        );
    }
    private boolean ensureTraversability(
            float playerXStartPos,
            float playerYStartPos,
            float playerWidth,
            float playerHeight,
            float horizontalSpeed
    ) {
        if (horizontalSpeed <= 0 || zonesPxSize <= 0 || playerWidth <= 0 || playerHeight <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        final int yCount = getHeight();
        final int xCount = getWidth();

        // 1. Start from the player's actual position
        final int startX = Math.max(0, Math.min(xCount - 1, (int) (playerXStartPos / zonesPxSize)));
        final int startY = Math.max(0, Math.min(yCount - 1, Math.round(playerYStartPos / zonesPxSize)));

        // Cap the right-most check to prevent bounds exceptions
        final int endX = xCount - 1 - (int) Math.floor((playerWidth - 0.001f) / zonesPxSize);

        if (endX <= startX) return true;

        final float dt = zonesPxSize / horizontalSpeed;

        // Calculate max vertical movement per column step
        final int cellsUp = (int) Math.ceil((MAX_PLAYER_UPWARDS_SPEED * dt) / zonesPxSize);
        final int cellsDown = (int) Math.ceil((MAX_PLAYER_FALL_SPEED * dt) / zonesPxSize);

        boolean[] currentReachable = new boolean[yCount];
        boolean[] nextReachable = new boolean[yCount];
        boolean[] safeY = new boolean[yCount];

        currentReachable[startY] = true;

        // 2. Scan forward to ensure a continuous path exists
        for (int x = startX + 1; x <= endX; ++x) {
            boolean anySafe = false;

            // OPTIMIZATION: Check collision for this column exactly ONCE per Y-cell.
            for (int y = 0; y < yCount; ++y) {
                safeY[y] = isCharacterFree(x * (float) zonesPxSize, y * (float) zonesPxSize, playerWidth, playerHeight);
                if (safeY[y]) anySafe = true;
            }

            // Early exit: If an obstacle completely walls off this entire column, it's impossible.
            if (!anySafe) return false;

            boolean anyReachable = false;
            java.util.Arrays.fill(nextReachable, false);

            for (int y = 0; y < yCount; ++y) {
                if (!currentReachable[y]) continue;

                // Expand reachable area based on max jump/fall speeds
                int minY = Math.max(0, y - cellsUp);
                int maxY = Math.min(yCount - 1, y + cellsDown);

                for (int nextY = minY; nextY <= maxY; ++nextY) {
                    // Only expand into cells that are safe and haven't been marked yet
                    if (!nextReachable[nextY] && safeY[nextY]) {
                        nextReachable[nextY] = true;
                        anyReachable = true;
                    }
                }
            }

            // If the reachable path collided completely with obstacles, reject the generation
            if (!anyReachable) return false;

            // Swap arrays for the next column
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
