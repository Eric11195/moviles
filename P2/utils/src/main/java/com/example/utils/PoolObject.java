package com.example.utils;

public abstract class PoolObject<T> implements mcd_Cloneable<T> {
    public int idx;
    public abstract T clone();
}
