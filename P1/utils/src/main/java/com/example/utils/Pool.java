package com.example.utils;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Pool <T extends PoolObject<T>>{
    ArrayList<T> items;

    PriorityQueue<Integer> available;
    int lastIndex = 0;

    int capacity;
    public Pool(int _capacity){
        capacity = _capacity;
        items = new ArrayList<>(capacity);
        available = new PriorityQueue<>();
    }
    public void fill(T elem){
        for (int i = 0; i<capacity; ++i){
            T temp = elem.clone();
            temp.idx = i;
            items.add(i, temp);
        }
    }

    public T fetchItem(){
        if (available.isEmpty()){
            if (lastIndex == capacity){
                throw new IndexOutOfBoundsException("Pool Depleted");
            }
            return items.get(lastIndex++);
        }
        else {
            return items.get(available.poll());
        }
    }

    public void releaseItem(T item){
        available.add(item.idx);
    }
}
