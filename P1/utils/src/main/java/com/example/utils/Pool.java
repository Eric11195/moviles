package com.example.utils;

import java.util.ArrayList;

public class Pool <T extends mcd_Cloneable<T>>{
    ArrayList<T> items;
    int capacity;
    Pool(int _capacity){
        capacity = _capacity;
        items = new ArrayList<>(capacity);
    }
    public void fill(T elem){
        for (int i = 0; i<capacity; ++i){
            items.add(i, (T)elem.clone());
        }
    }
    boolean isFull(){
        return items.size() == capacity;
    }

    public T fetchItem (){
        for (int i = 0; i<capacity; ++i){
            if (items.get(i) != null) {
                items.add(i,null);
                return items.get(i);
            }
        }
        return null;
    }

    public void releaseItem(T item){
        int i = 0;
        while (i<capacity && items.get(i) != null) ++i;
        if (i==capacity) return;
        items.add(i,item);
    }
}
