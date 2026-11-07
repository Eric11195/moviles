
package com.example.game.testing.Systems.menu;

import static com.example.game.testing.constants.Constants.BACK_BUTTON_SQUARE_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.mcd_System;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import java.util.List;

public class Scrollable implements mcd_System {

    private final int rows;
    private final int totalRows;
    private final int cols;

    private final Vec2 startPos;
    private final Vec2 size;

    private final List<mcd_Entity> allMyObjects;

    // Normalized scrollbar position [0, 1].
    private float barProgress;

    // Scrollbar thumb height in pixels.
    private float barSize;

    // Scrollbar geometry.
    private final Vec2 scrollBarPos = new Vec2();
    private final Vec2 scrollBarSize =
            new Vec2(BACK_BUTTON_SQUARE_SIZE * 0.8f, 0);

    // Input.
    private float mouseStampY = -1;

    // Available vertical space for content and scrollbar.
    private final float viewportHeight;

    public Scrollable(
            Vec2 startPos,
            Vec2 size,
            int rows,
            int cols,
            List<mcd_Entity> trList) {

        this.startPos = startPos;
        this.size = size;

        this.rows = Math.max(1, rows);
        this.cols = Math.max(1, cols);

        this.allMyObjects = trList;

        // Calculate the actual number of rows required.
        this.totalRows = Math.max(
                this.rows,
                (trList.size() + this.cols - 1) / this.cols
        );

        // Reserve space for the bottom button.
        viewportHeight = Math.max(
                0,
                size.y - BACK_BUTTON_SQUARE_SIZE
        );

        this.barProgress = 0;

        // Calculate scrollbar thumb height.
        this.barSize = viewportHeight *
                this.rows / (float) this.totalRows;

        this.scrollBarSize.y = barSize;
    }

    private float getMaxScroll() {
        return totalRows - rows;
    }

    private float getAvailableBarMovement() {
        return Math.max(0, viewportHeight - barSize);
    }

    private float clamp01(float value) {
        return Math.max(0, Math.min(1, value));
    }

    // =====================================================
    // INPUT
    // =====================================================

    private void handleMouseInput(Engine eng) {

        for (TouchEvent evt : eng.getInput().getTouchEvents()) {

            switch (evt.type.numVal()) {

                case TouchEventType.DOWN_VALUE:

                    mouseStampY = evt.pos.y;
                    break;

                case TouchEventType.UP_VALUE:

                    if (mouseStampY >= 0) {

                        float delta = evt.pos.y - mouseStampY;

                        float availableMovement =
                                getAvailableBarMovement();

                        if (availableMovement > 0) {

                            barProgress +=
                                    delta / availableMovement;

                            barProgress = clamp01(barProgress);
                        }
                    }

                    mouseStampY = -1;
                    break;

                default:
                    break;
            }
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public void update(mcd_Scene scene, float t, float dt) {

        // Process input every frame.
        handleMouseInput(scene.getEngine());

        // Calculate how many rows have been scrolled.
        float scrollOffset = barProgress * getMaxScroll();

        // Calculate the vertical distance between rows.
        float rowHeight = size.y / rows;

        int i = 0;

        while (i < allMyObjects.size()) {

            int row = i / cols;

            // Apply scroll offset to the row.
            float visibleRow = row - scrollOffset;

            float y = startPos.y + rowHeight * visibleRow;

            int limit = Math.min(
                    i + cols,
                    allMyObjects.size()
            );

            for (int j = i; j < limit; ++j) {

                int col = j - i;

                float x = startPos.x +
                        size.x * col / (float) cols;

                mcd_Entity ent = allMyObjects.get(j);

                TransformComponent tr =
                        (TransformComponent) ent.getComponent(
                                ComponentId.TRANSFORM_COMPONENT
                        );

                tr.pos.x = x;
                tr.pos.y = y;
            }

            i += cols;
        }

        // Update inner objects.
        allMyObjects.forEach(ent -> ent.update(t, dt));
    }

    // =====================================================
    // RENDER
    // =====================================================

    @Override
    public void render(GraphicsInterface g) {

        // Render inner objects.
        allMyObjects.forEach(ent -> ent.render(g));

        // Calculate scrollbar position.
        scrollBarPos.x =
                startPos.x - BACK_BUTTON_SQUARE_SIZE * 0.9f;

        scrollBarPos.y =
                startPos.y +
                        barProgress * getAvailableBarMovement();

        // Render scrollbar.
        g.setColor(ColorLibrary.DarkGrey);

        g.drawRoundRectangle(
                scrollBarPos,
                scrollBarSize,
                90,
                true
        );
    }

    // =====================================================
    // ACTIVATION
    // =====================================================

    public void setActive(boolean value) {

        allMyObjects.forEach(ent -> ent.setActive(value));
    }
}
