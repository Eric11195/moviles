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
            playerTr.pos.x, playerTr.pos.y,
            playerTr.size.x+PLAYER_MARGINS.x, playerTr.size.y+PLAYER_MARGINS.y,
            playerVel.velocity.y,
            LevelSpeedSystem.getInstance().getSpeedMult()
        );
    }

    private boolean ensureTraversability(
            float playerXStartPos,
            float playerYStartPos,
            float playerWidth,
            float playerHeight,
            float initialVy,
            float horizontalSpeed
    ) {
        if (horizontalSpeed <= 0 || zonesPxSize <= 0
                || playerWidth <= 0 || playerHeight <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        // Coarse velocity quantization to reduce the state count.
        final float vyStep = 50f;
        final float dt = zonesPxSize / horizontalSpeed;

        final int yCount = getHeight();
        final int vCount = 2 * (int) Math.ceil(MAX_PLAYER_UPWARDS_SPEED / vyStep) + 1;
        final int zeroV = vCount / 2;

        final int startX = Math.min(getWidth() - 1,
                Math.max(0, (int) (playerXStartPos / zonesPxSize)));

        final int startY = Math.round(playerYStartPos / zonesPxSize);
        final int startV = Math.max(0, Math.min(vCount - 1,
                Math.round(initialVy / vyStep) + zeroV));

        if (startY < 0 || startY >= yCount) return false;

        boolean[][] current = new boolean[yCount][vCount];
        boolean[][] next = new boolean[yCount][vCount];

        current[startY][startV] = true;

        // Reused collision lookup.
        boolean[] safeY = new boolean[yCount];

        for (int x = startX; x >= 0; --x) {
            // Precompute valid character positions in this column.
            for (int y = 0; y < yCount; ++y) {
                safeY[y] = isCharacterFree(
                        x * (float) zonesPxSize,
                        y * (float) zonesPxSize,
                        playerWidth,
                        playerHeight
                );
            }

            // Discard states that are no longer collision-free.
            boolean any = false;

            for (int y = 0; y < yCount; ++y) {
                for (int v = 0; v < vCount; ++v) {
                    if (!current[y][v]) continue;

                    if (!safeY[y]) {
                        current[y][v] = false;
                    } else {
                        any = true;
                    }
                }
            }

            if (!any) return false;

            if (x == 0) return true;

            // Clear the reusable destination array.
            for (int y = 0; y < yCount; ++y) {
                java.util.Arrays.fill(next[y], false);
            }

            for (int y = 0; y < yCount; ++y) {
                for (int v = 0; v < vCount; ++v) {
                    if (!current[y][v]) continue;

                    float vy = (v - zeroV) * vyStep;

                    // Two possible actions: gravity or thrust.
                    for (int action = 0; action < 2; ++action) {
                        float acceleration = GRAVITY_FORCE
                                - (action == 1 ? Y_PLAYER_ACCEL : 0);

                        float newVy = Math.max(-MAX_PLAYER_UPWARDS_SPEED,
                                Math.min(MAX_PLAYER_FALL_SPEED,
                                        vy + acceleration * dt));

                        float newY = y * (float) zonesPxSize
                                + newVy * dt;

                        int ny = Math.round(newY / zonesPxSize);
                        int nv = Math.max(0, Math.min(vCount - 1,
                                Math.round(newVy / vyStep) + zeroV));

                        if (ny < 0 || ny >= yCount) continue;
                        if (!safeY[ny]) continue;

                        next[ny][nv] = true;
                    }
                }
            }

            boolean[][] temp = current;
            current = next;
            next = temp;
        }

        return false;
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
