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
package com.example.utils;

import java.io.Serializable;

/**
 * A 2D column vector with floating-point components.
 */
public class Vec2 implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Horizontal component. */
    public float x;

    /** Vertical component. */
    public float y;

    /**
     * Constructs a default zero vector (0, 0).
     */
    public Vec2() {
        this(0, 0);
    }

    /**
     * Constructs a vector with the specified x and y components.
     *
     * @param x horizontal component
     * @param y vertical component
     */
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     *
     * @param toCopy vector to copy components from
     */
    public Vec2(Vec2 toCopy) {
        this(toCopy.x, toCopy.y);
    }

    /** Zero out this vector. */
    public final void setZero() {
        x = 0.0f;
        y = 0.0f;
    }

    /**
     * Set the vector component-wise.
     *
     * @param x horizontal component
     * @param y vertical component
     * @return this vector for chaining
     */
    public final Vec2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Set this vector to another vector.
     *
     * @param v vector to copy from
     * @return this vector for chaining
     */
    public final Vec2 set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    /**
     * Return the sum of this vector and another; does not alter either one.
     *
     * @param v vector to add
     * @return new vector containing the sum
     */
    public final Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    /**
     * Return the difference of this vector and another; does not alter either one.
     *
     * @param v vector to subtract
     * @return new vector containing the difference
     */
    public final Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    /**
     * Return this vector multiplied by a scalar; does not alter this vector.
     *
     * @param a scalar multiplier
     * @return new scaled vector
     */
    public final Vec2 mul(float a) {
        return new Vec2(x * a, y * a);
    }

    /**
     * Return the negation of this vector; does not alter this vector.
     *
     * @return new negated vector
     */
    public final Vec2 negate() {
        return new Vec2(-x, -y);
    }

    /**
     * Flip the vector and return it - alters this vector.
     *
     * @return this vector for chaining
     */
    public final Vec2 negateLocal() {
        x = -x;
        y = -y;
        return this;
    }

    /**
     * Add another vector to this one - alters this vector.
     *
     * @param v vector to add
     * @return this vector for chaining
     */
    public final Vec2 addLocal(Vec2 v) {
        x += v.x;
        y += v.y;
        return this;
    }

    /**
     * Adds components to this vector - alters this vector.
     *
     * @param x horizontal component to add
     * @param y vertical component to add
     * @return this vector for chaining
     */
    public final Vec2 addLocal(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Subtract another vector from this one - alters this vector.
     *
     * @param v vector to subtract
     * @return this vector for chaining
     */
    public final Vec2 subLocal(Vec2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    /**
     * Multiply this vector by a scalar - alters this vector.
     *
     * @param a scalar multiplier
     * @return this vector for chaining
     */
    public final Vec2 mulLocal(float a) {
        x *= a;
        y *= a;
        return this;
    }

    /**
     * Get the skew vector (-y, x).
     *
     * @return new skew vector
     */
    public final Vec2 skew() {
        return new Vec2(-y, x);
    }

    /**
     * Store the skew vector (-y, x) into the given output vector.
     *
     * @param out output destination vector
     */
    public final void skew(Vec2 out) {
        out.x = -y;
        out.y = x;
    }

    /**
     * Return the length of this vector.
     *
     * @return Euclidean length
     */
    public final float length() {
        return MathUtils.sqrt(x * x + y * y);
    }

    /**
     * Return the squared length of this vector.
     *
     * @return squared length
     */
    public final float lengthSquared() {
        return (x * x + y * y);
    }

    /**
     * Normalize this vector and return the length before normalization. Alters this vector.
     *
     * @return original length before normalization
     */
    public final float normalize() {
        float length = length();
        if (length < Settings.EPSILON) {
            return 0f;
        }

        float invLength = 1.0f / length;
        x *= invLength;
        y *= invLength;
        return length;
    }

    /**
     * Checks whether the vector contains valid (non-NaN, non-infinite) floating-point numbers.
     *
     * @return true if valid, false otherwise
     */
    public final boolean isValid() {
        return !Float.isNaN(x) && !Float.isInfinite(x) && !Float.isNaN(y) && !Float.isInfinite(y);
    }

    /**
     * Return a new vector that has positive components.
     *
     * @return new vector with absolute values of components
     */
    public final Vec2 abs() {
        return new Vec2(MathUtils.abs(x), MathUtils.abs(y));
    }

    /**
     * Applies absolute value to each component - alters this vector.
     */
    public final void absLocal() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
    }

    /**
     * Return a copy of this vector.
     *
     * @return new cloned vector instance
     */
    @Override
    public final Vec2 clone() {
        return new Vec2(x, y);
    }

    /**
     * Returns a string representation of this vector in (x,y) format.
     *
     * @return string representation
     */
    @Override
    public final String toString() {
        return "(" + x + "," + y + ")";
    }

    /*
     * Static methods
     */

    /**
     * Returns a new vector containing the absolute values of a vector's components.
     *
     * @param a input vector
     * @return new absolute vector
     */
    public final static Vec2 abs(Vec2 a) {
        return new Vec2(MathUtils.abs(a.x), MathUtils.abs(a.y));
    }

    /**
     * Stores absolute values of vector 'a' into destination vector 'out'.
     *
     * @param a input vector
     * @param out output vector
     */
    public final static void absToOut(Vec2 a, Vec2 out) {
        out.x = MathUtils.abs(a.x);
        out.y = MathUtils.abs(a.y);
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param a first vector
     * @param b second vector
     * @return dot product
     */
    public final static float dot(final Vec2 a, final Vec2 b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Calculates the 2D cross product of two vectors (scalar result).
     *
     * @param a first vector
     * @param b second vector
     * @return cross product scalar value
     */
    public final static float cross(final Vec2 a, final Vec2 b) {
        return a.x * b.y - a.y * b.x;
    }

    /**
     * Calculates cross product of a vector and a scalar.
     *
     * @param a input vector
     * @param s scalar value
     * @return new cross product vector
     */
    public final static Vec2 cross(Vec2 a, float s) {
        return new Vec2(s * a.y, -s * a.x);
    }

    /**
     * Stores cross product of a vector and scalar into destination vector.
     *
     * @param a input vector
     * @param s scalar value
     * @param out destination vector
     */
    public final static void crossToOut(Vec2 a, float s, Vec2 out) {
        final float tempy = -s * a.x;
        out.x = s * a.y;
        out.y = tempy;
    }

    /**
     * Unsafe version of cross product storing into destination vector. Asserts out != a.
     *
     * @param a input vector
     * @param s scalar value
     * @param out destination vector
     */
    public final static void crossToOutUnsafe(Vec2 a, float s, Vec2 out) {
        assert (out != a);
        out.x = s * a.y;
        out.y = -s * a.x;
    }

    /**
     * Calculates cross product of a scalar and a vector.
     *
     * @param s scalar value
     * @param a input vector
     * @return new cross product vector
     */
    public final static Vec2 cross(float s, Vec2 a) {
        return new Vec2(-s * a.y, s * a.x);
    }

    /**
     * Stores cross product of a scalar and vector into destination vector.
     *
     * @param s scalar value
     * @param a input vector
     * @param out destination vector
     */
    public final static void crossToOut(float s, Vec2 a, Vec2 out) {
        final float tempY = s * a.x;
        out.x = -s * a.y;
        out.y = tempY;
    }

    /**
     * Unsafe cross product of scalar and vector storing into destination vector. Asserts out != a.
     *
     * @param s scalar value
     * @param a input vector
     * @param out destination vector
     */
    public final static void crossToOutUnsafe(float s, Vec2 a, Vec2 out) {
        assert (out != a);
        out.x = -s * a.y;
        out.y = s * a.x;
    }

    /**
     * Negates a vector and stores the result in destination vector 'out'.
     *
     * @param a input vector
     * @param out destination vector
     */
    public final static void negateToOut(Vec2 a, Vec2 out) {
        out.x = -a.x;
        out.y = -a.y;
    }

    /**
     * Returns a new vector containing the component-wise minimum of two vectors.
     *
     * @param a first vector
     * @param b second vector
     * @return minimum vector
     */
    public final static Vec2 min(Vec2 a, Vec2 b) {
        return new Vec2(a.x < b.x ? a.x : b.x, a.y < b.y ? a.y : b.y);
    }

    /**
     * Returns a new vector containing the component-wise maximum of two vectors.
     *
     * @param a first vector
     * @param b second vector
     * @return maximum vector
     */
    public final static Vec2 max(Vec2 a, Vec2 b) {
        return new Vec2(a.x > b.x ? a.x : b.x, a.y > b.y ? a.y : b.y);
    }

    /**
     * Stores component-wise minimum of two vectors into destination vector.
     *
     * @param a first vector
     * @param b second vector
     * @param out destination vector
     */
    public final static void minToOut(Vec2 a, Vec2 b, Vec2 out) {
        out.x = a.x < b.x ? a.x : b.x;
        out.y = a.y < b.y ? a.y : b.y;
    }

    /**
     * Stores component-wise maximum of two vectors into destination vector.
     *
     * @param a first vector
     * @param b second vector
     * @param out destination vector
     */
    public final static void maxToOut(Vec2 a, Vec2 b, Vec2 out) {
        out.x = a.x > b.x ? a.x : b.x;
        out.y = a.y > b.y ? a.y : b.y;
    }

    /**
     * Generates a hash code based on x and y components.
     *
     * @return hash code
     */
    @Override
    public int hashCode() { // automatically generated by Eclipse
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    /**
     * Compares equality of this vector with another object.
     *
     * @param obj target object for comparison
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) { // automatically generated by Eclipse
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vec2 other = (Vec2) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
        return true;
    }
}
