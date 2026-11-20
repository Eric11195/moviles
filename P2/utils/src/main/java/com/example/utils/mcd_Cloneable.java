package com.example.utils;

/**
 * Generic interface for cloneable objects.
 *
 * @param <T> the type of object returned by the clone operation
 */
public interface mcd_Cloneable<T> {

    /**
     * Creates and returns a copy of this object.
     *
     * @return cloned instance of type T
     */
    public T clone();
}
