package com.example.game.testing.utils;

import static com.example.utils.MathUtils.cos;
import static com.example.utils.MathUtils.sin;

import com.example.utils.Vec2;

import java.util.List;

public class Rotator {
    /**
     * Rotates point from center degreesRot degrees.
     * DOES NOT MODIFY POINT
     * @param degreesRot the rotation to be applied in degrees
     * @param center the center from which the rotation will be applied
     * @param point the point to be rotated
     * @return
     */
    public static <T extends Vec2> Vec2 rotate(float degreesRot, Vec2 center, T point){
        return new Vec2(
            center.x + (point.x-center.x)*cos(degreesRot)-(point.y-center.y)*sin(degreesRot),
            center.y + (point.x-center.x)*sin(degreesRot)+(point.y-center.y)*cos(degreesRot)
        );
    }

    /**
     * Rotates a list of points from "center" "degreesRot" degrees
     * THIS MODIFIES THE LIST "points"
     * @param degreesRot the rotation to be applied in degrees
     * @param center the center from which the rotation will be applied
     * @param points the list of points to be rotated
     */
    public static <T extends Vec2> void rotate(float degreesRot, Vec2 center, List<T> points){
        points.forEach(point -> {
            Vec2 result =rotate(degreesRot,center,point);
            point.x = result.x;
            point.y = result.y;
        });
    }
}
