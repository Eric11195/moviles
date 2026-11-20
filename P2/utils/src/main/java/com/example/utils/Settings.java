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

/**
 * Global tuning constants based on MKS units and various integer maximums (vertices per shape,
 * pairs, etc.).
 */
public class Settings {

    /** A "close to zero" float epsilon value for use */
    public static final float EPSILON = 1.1920928955078125E-7f;

    /** Pi. */
    public static final float PI = (float) Math.PI;

    // JBox2D specific settings
    /** Enable fast absolute value function. */
    public static boolean FAST_ABS = true;

    /** Enable fast floor function. */
    public static boolean FAST_FLOOR = true;

    /** Enable fast ceiling function. */
    public static boolean FAST_CEIL = true;

    /** Enable fast rounding function. */
    public static boolean FAST_ROUND = true;

    /** Enable fast atan2 function. */
    public static boolean FAST_ATAN2 = true;

    /** Enable fast power function. */
    public static boolean FAST_POW = true;

    /** Initial size for contact stack. */
    public static int CONTACT_STACK_INIT_SIZE = 10;

    /** Enable sine/cosine lookup table. */
    public static boolean SINCOS_LUT_ENABLED = true;

    /**
     * Precision for sine/cosine lookup table.
     */
    public static final float SINCOS_LUT_PRECISION = .00011f;

    /** Length of sine/cosine lookup table. */
    public static final int SINCOS_LUT_LENGTH = (int) Math.ceil(Math.PI * 2 / SINCOS_LUT_PRECISION);

    /**
     * Use linear interpolation for lookup table values.
     */
    public static boolean SINCOS_LUT_LERP = false;


    // Collision

    /**
     * The maximum number of contact points between two convex shapes.
     */
    public static int maxManifoldPoints = 2;

    /**
     * The maximum number of vertices on a convex polygon.
     */
    public static int maxPolygonVertices = 8;

    /**
     * This is used to fatten AABBs in the dynamic tree. This allows proxies to move by a small amount
     * without triggering a tree adjustment. This is in meters.
     */
    public static float aabbExtension = 0.1f;

    /**
     * This is used to fatten AABBs in the dynamic tree. This is used to predict the future position
     * based on the current displacement. This is a dimensionless multiplier.
     */
    public static float aabbMultiplier = 2.0f;

    /**
     * A small length used as a collision and constraint tolerance. Usually it is chosen to be
     * numerically significant, but visually insignificant.
     */
    public static float linearSlop = 0.005f;

    /**
     * A small angle used as a collision and constraint tolerance. Usually it is chosen to be
     * numerically significant, but visually insignificant.
     */
    public static float angularSlop = (2.0f / 180.0f * PI);

    /**
     * The radius of the polygon/edge shape skin. This should not be modified. Making this smaller
     * means polygons will have and insufficient for continuous collision. Making it larger may create
     * artifacts for vertex collision.
     */
    public static float polygonRadius = (2.0f * linearSlop);

    /** Maximum number of sub-steps per contact in continuous physics simulation. */
    public static int maxSubSteps = 8;

    // Dynamics

    /**
     * Maximum number of contacts to be handled to solve a TOI island.
     */
    public static int maxTOIContacts = 32;

    /**
     * A velocity threshold for elastic collisions. Any collision with a relative linear velocity
     * below this threshold will be treated as inelastic.
     */
    public static float velocityThreshold = 1.0f;

    /**
     * The maximum linear position correction used when solving constraints. This helps to prevent
     * overshoot.
     */
    public static float maxLinearCorrection = 0.2f;

    /**
     * The maximum angular position correction used when solving constraints. This helps to prevent
     * overshoot.
     */
    public static float maxAngularCorrection = (8.0f / 180.0f * PI);

    /**
     * The maximum linear velocity of a body. This limit is very large and is used to prevent
     * numerical problems. You shouldn't need to adjust this.
     */
    public static float maxTranslation = 2.0f;

    /** Squared maximum translation. */
    public static float maxTranslationSquared = (maxTranslation * maxTranslation);

    /**
     * The maximum angular velocity of a body. This limit is very large and is used to prevent
     * numerical problems. You shouldn't need to adjust this.
     */
    public static float maxRotation = (0.5f * PI);

    /** Squared maximum rotation. */
    public static float maxRotationSquared = (maxRotation * maxRotation);

    /**
     * This scale factor controls how fast overlap is resolved. Ideally this would be 1 so that
     * overlap is removed in one time step. However using values close to 1 often lead to overshoot.
     */
    public static float baumgarte = 0.2f;

    /** TOI Baumgarte parameter. */
    public static float toiBaugarte = 0.75f;


    // Sleep

    /**
     * The time that a body must be still before it will go to sleep.
     */
    public static float timeToSleep = 0.5f;

    /**
     * A body cannot sleep if its linear velocity is above this tolerance.
     */
    public static float linearSleepTolerance = 0.01f;

    /**
     * A body cannot sleep if its angular velocity is above this tolerance.
     */
    public static float angularSleepTolerance = (2.0f / 180.0f * PI);

    // Particle

    /**
     * A symbolic constant that stands for particle allocation error.
     */
    public static final int invalidParticleIndex = (-1);

    /**
     * The standard distance between particles, divided by the particle radius.
     */
    public static final float particleStride = 0.75f;

    /**
     * The minimum particle weight that produces pressure.
     */
    public static final float minParticleWeight = 1.0f;

    /**
     * The upper limit for particle weight used in pressure calculation.
     */
    public static final float maxParticleWeight = 5.0f;

    /**
     * The maximum distance between particles in a triad, divided by the particle radius.
     */
    public static final int maxTriadDistance = 2;

    /** Squared maximum triad distance. */
    public static final int maxTriadDistanceSquared = (maxTriadDistance * maxTriadDistance);

    /**
     * The initial size of particle data buffers.
     */
    public static final int minParticleBufferCapacity = 256;


    /**
     * Friction mixing law.
     *
     * @param friction1 first friction coefficient
     * @param friction2 second friction coefficient
     * @return mixed friction coefficient
     */
    public static float mixFriction(float friction1, float friction2) {
        return MathUtils.sqrt(friction1 * friction2);
    }

    /**
     * Restitution mixing law.
     *
     * @param restitution1 first restitution coefficient
     * @param restitution2 second restitution coefficient
     * @return mixed restitution coefficient
     */
    public static float mixRestitution(float restitution1, float restitution2) {
        return restitution1 > restitution2 ? restitution1 : restitution2;
    }
}
