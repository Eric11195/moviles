package com.example.utils;

/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 *
 * JBox2D homepage: http://jbox2d.sourceforge.net/
 * Box2D homepage: http://www.box2d.org
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

import java.util.Random;

/**
 * A collection of utility mathematical routines and fast approximations.
 */
public class MathUtils extends PlatformMathUtils {

    /** Value of PI as a float. */
    public static final float PI = (float) Math.PI;

    /** Value of 2 * PI as a float. */
    public static final float TWOPI = (float) (Math.PI * 2);

    /** Value of 1 / PI as a float. */
    public static final float INV_PI = 1f / PI;

    /** Value of PI / 2 as a float. */
    public static final float HALF_PI = PI / 2;

    /** Value of PI / 4 as a float. */
    public static final float QUARTER_PI = PI / 4;

    /** Value of 3 * PI / 2 as a float. */
    public static final float THREE_HALVES_PI = TWOPI - HALF_PI;

    /**
     * Degrees to radians conversion factor.
     */
    public static final float DEG2RAD = PI / 180;

    /**
     * Radians to degrees conversion factor.
     */
    public static final float RAD2DEG = 180 / PI;

    /**
     * Lookup table for sine values.
     */
    public static final float[] sinLUT = new float[Settings.SINCOS_LUT_LENGTH];

    static {
        for (int i = 0; i < Settings.SINCOS_LUT_LENGTH; i++) {
            sinLUT[i] = (float) Math.sin(i * Settings.SINCOS_LUT_PRECISION);
        }
    }

    /**
     * Computes the sine of an angle in radians using a lookup table or StrictMath.
     *
     * @param x angle in radians
     * @return sine of x
     */
    public static final float sin(float x) {
        if (Settings.SINCOS_LUT_ENABLED) {
            return sinLUT(x);
        } else {
            return (float) StrictMath.sin(x);
        }
    }

    /**
     * Computes sine using the lookup table.
     *
     * @param x angle in radians
     * @return sine of x from lookup table
     */
    public static final float sinLUT(float x) {
        x %= TWOPI;

        if (x < 0) {
            x += TWOPI;
        }

        if (Settings.SINCOS_LUT_LERP) {

            x /= Settings.SINCOS_LUT_PRECISION;

            final int index = (int) x;

            if (index != 0) {
                x %= index;
            }

            // the next index is 0
            if (index == Settings.SINCOS_LUT_LENGTH - 1) {
                return ((1 - x) * sinLUT[index] + x * sinLUT[0]);
            } else {
                return ((1 - x) * sinLUT[index] + x * sinLUT[index + 1]);
            }

        } else {
            return sinLUT[MathUtils.round(x / Settings.SINCOS_LUT_PRECISION) % Settings.SINCOS_LUT_LENGTH];
        }
    }

    /**
     * Computes the cosine of an angle in radians using a lookup table or StrictMath.
     *
     * @param x angle in radians
     * @return cosine of x
     */
    public static final float cos(float x) {
        if (Settings.SINCOS_LUT_ENABLED) {
            return sinLUT(HALF_PI - x);
        } else {
            return (float) StrictMath.cos(x);
        }
    }

    /**
     * Returns the absolute value of a float.
     *
     * @param x input float value
     * @return absolute value of x
     */
    public static final float abs(final float x) {
        if (Settings.FAST_ABS) {
            return x > 0 ? x : -x;
        } else {
            return StrictMath.abs(x);
        }
    }

    /**
     * Fast approximation of absolute value for a float.
     *
     * @param x input float value
     * @return absolute value of x
     */
    public static final float fastAbs(final float x) {
        return x > 0 ? x : -x;
    }

    /**
     * Fast absolute value calculation for an integer.
     *
     * @param x input integer value
     * @return absolute value of x
     */
    public static final int abs(int x) {
        int y = x >> 31;
        return (x ^ y) - y;
    }

    /**
     * Returns the floor of a float value.
     *
     * @param x input float value
     * @return floor value as an integer
     */
    public static final int floor(final float x) {
        if (Settings.FAST_FLOOR) {
            return fastFloor(x);
        } else {
            return (int) StrictMath.floor(x);
        }
    }

    /**
     * Fast floor implementation for a float value.
     *
     * @param x input float value
     * @return floor value as an integer
     */
    public static final int fastFloor(final float x) {
        int y = (int) x;
        if (x < y) {
            return y - 1;
        }
        return y;
    }

    /**
     * Returns the ceiling of a float value.
     *
     * @param x input float value
     * @return ceiling value as an integer
     */
    public static final int ceil(final float x) {
        if (Settings.FAST_CEIL) {
            return fastCeil(x);
        } else {
            return (int) StrictMath.ceil(x);
        }
    }

    /**
     * Fast ceiling implementation for a float value.
     *
     * @param x input float value
     * @return ceiling value as an integer
     */
    public static final int fastCeil(final float x) {
        int y = (int) x;
        if (x > y) {
            return y + 1;
        }
        return y;
    }

    /**
     * Rounds a float value to the nearest integer.
     *
     * @param x input float value
     * @return rounded integer value
     */
    public static final int round(final float x) {
        if (Settings.FAST_ROUND) {
            return floor(x + .5f);
        } else {
            return StrictMath.round(x);
        }
    }

    /**
     * Rounds up the value to the nearest higher power of 2 value.
     *
     * @param x input integer
     * @return nearest higher power of 2 value
     */
    public static final int ceilPowerOf2(int x) {
        int pow2 = 1;
        while (pow2 < x) {
            pow2 <<= 1;
        }
        return pow2;
    }

    /**
     * Returns the maximum of two float values.
     *
     * @param a first float
     * @param b second float
     * @return maximum of a and b
     */
    public final static float max(final float a, final float b) {
        return a > b ? a : b;
    }

    /**
     * Returns the maximum of two integer values.
     *
     * @param a first integer
     * @param b second integer
     * @return maximum of a and b
     */
    public final static int max(final int a, final int b) {
        return a > b ? a : b;
    }

    /**
     * Returns the minimum of two float values.
     *
     * @param a first float
     * @param b second float
     * @return minimum of a and b
     */
    public final static float min(final float a, final float b) {
        return a < b ? a : b;
    }

    /**
     * Returns the minimum of two integer values.
     *
     * @param a first integer
     * @param b second integer
     * @return minimum of a and b
     */
    public final static int min(final int a, final int b) {
        return a < b ? a : b;
    }

    /**
     * Maps a float value from one range to another.
     *
     * @param val input value
     * @param fromMin original range lower bound
     * @param fromMax original range upper bound
     * @param toMin target range lower bound
     * @param toMax target range upper bound
     * @return mapped value in target range
     */
    public final static float map(final float val, final float fromMin, final float fromMax,
                                  final float toMin, final float toMax) {
        final float mult = (val - fromMin) / (fromMax - fromMin);
        final float res = toMin + mult * (toMax - toMin);
        return res;
    }

    /**
     * Returns the closest float value to 'a' that is between 'low' and 'high'.
     *
     * @param a value to clamp
     * @param low lower bound
     * @param high upper bound
     * @return clamped value
     */
    public final static float clamp(final float a, final float low, final float high) {
        return max(low, min(a, high));
    }

    /**
     * Clamps a 2D vector within lower and upper bound 2D vectors.
     *
     * @param a vector to clamp
     * @param low lower bound vector
     * @param high upper bound vector
     * @return new clamped vector
     */
    public final static Vec2 clamp(final Vec2 a, final Vec2 low, final Vec2 high) {
        final Vec2 min = new Vec2();
        min.x = a.x < high.x ? a.x : high.x;
        min.y = a.y < high.y ? a.y : high.y;
        min.x = low.x > min.x ? low.x : min.x;
        min.y = low.y > min.y ? low.y : min.y;
        return min;
    }

    /**
     * Clamps a 2D vector within lower and upper bounds and stores the result in a destination vector.
     *
     * @param a vector to clamp
     * @param low lower bound vector
     * @param high upper bound vector
     * @param dest destination vector for the result
     */
    public final static void clampToOut(final Vec2 a, final Vec2 low, final Vec2 high, final Vec2 dest) {
        dest.x = a.x < high.x ? a.x : high.x;
        dest.y = a.y < high.y ? a.y : high.y;
        dest.x = low.x > dest.x ? low.x : dest.x;
        dest.y = low.y > dest.y ? low.y : dest.y;
    }

    /**
     * Computes the next largest power of 2 for a given integer.
     *
     * @param x input integer
     * @return next largest power of 2
     */
    public final static int nextPowerOfTwo(int x) {
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }

    /**
     * Checks if an integer is a power of 2.
     *
     * @param x input integer
     * @return true if x is a power of 2, false otherwise
     */
    public final static boolean isPowerOfTwo(final int x) {
        return x > 0 && (x & x - 1) == 0;
    }

    /**
     * Computes the power of a base float raised to an exponent float.
     *
     * @param a base
     * @param b exponent
     * @return a raised to power b
     */
    public static final float pow(float a, float b) {
        if (Settings.FAST_POW) {
            return fastPow(a, b);
        } else {
            return (float) StrictMath.pow(a, b);
        }
    }

    /**
     * Computes atan2 of y and x.
     *
     * @param y ordinate
     * @param x abscissa
     * @return angle in radians
     */
    public static final float atan2(final float y, final float x) {
        if (Settings.FAST_ATAN2) {
            return fastAtan2(y, x);
        } else {
            return (float) StrictMath.atan2(y, x);
        }
    }

    /**
     * Fast approximation of atan2.
     *
     * @param y ordinate
     * @param x abscissa
     * @return fast approximation of angle in radians
     */
    public static final float fastAtan2(float y, float x) {
        if (x == 0.0f) {
            if (y > 0.0f) return HALF_PI;
            if (y == 0.0f) return 0.0f;
            return -HALF_PI;
        }
        float atan;
        final float z = y / x;
        if (abs(z) < 1.0f) {
            atan = z / (1.0f + 0.28f * z * z);
            if (x < 0.0f) {
                if (y < 0.0f) return atan - PI;
                return atan + PI;
            }
        } else {
            atan = HALF_PI - z / (z * z + 0.28f);
            if (y < 0.0f) return atan - PI;
        }
        return atan;
    }

    /**
     * Reduces an angle in radians to the range [-PI, PI].
     *
     * @param theta angle in radians
     * @return reduced angle in radians
     */
    public static final float reduceAngle(float theta) {
        theta %= TWOPI;
        if (abs(theta) > PI) {
            theta = theta - TWOPI;
        }
        if (abs(theta) > HALF_PI) {
            theta = PI - theta;
        }
        return theta;
    }

    /**
     * Generates a pseudo-random float between argLow and argHigh.
     *
     * @param argLow lower bound (inclusive)
     * @param argHigh upper bound (exclusive)
     * @return random float between argLow and argHigh
     */
    public static final float randomFloat(float argLow, float argHigh) {
        return (float) Math.random() * (argHigh - argLow) + argLow;
    }

    /**
     * Generates a pseudo-random float using a given Random instance.
     *
     * @param r random number generator
     * @param argLow lower bound (inclusive)
     * @param argHigh upper bound (exclusive)
     * @return random float between argLow and argHigh
     */
    public static final float randomFloat(Random r, float argLow, float argHigh) {
        return r.nextFloat() * (argHigh - argLow) + argLow;
    }

    /**
     * Computes the square root of a float.
     *
     * @param x input float value
     * @return square root of x
     */
    public static final float sqrt(float x) {
        return (float) StrictMath.sqrt(x);
    }

    /**
     * Computes the squared distance between two 2D vectors.
     *
     * @param v1 first 2D vector
     * @param v2 second 2D vector
     * @return squared distance between v1 and v2
     */
    public final static float distanceSquared(Vec2 v1, Vec2 v2) {
        float dx = (v1.x - v2.x);
        float dy = (v1.y - v2.y);
        return dx * dx + dy * dy;
    }

    /**
     * Computes the Euclidean distance between two 2D vectors.
     *
     * @param v1 first 2D vector
     * @param v2 second 2D vector
     * @return distance between v1 and v2
     */
    public final static float distance(Vec2 v1, Vec2 v2) {
        return sqrt(distanceSquared(v1, v2));
    }
}
