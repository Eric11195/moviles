package com.example.utils;

/**
 * Base abstract class for objects managed by a {@link Pool}.
 *
 * @param <T> the concrete pool object type
 */
public abstract class PoolObject<T> implements mcd_Cloneable<T> {

    /** Index of the item within its parent pool. */
    public int idx;

    /**
     * Creates a deep copy of this object.
     *
     * @return cloned instance of type T
     */
    @Override
    public abstract T clone();
}
