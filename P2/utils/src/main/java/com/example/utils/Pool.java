package com.example.utils;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Generic object pool for reusing objects of type {@link PoolObject}.
 *
 * @param <T> the pooled object type
 */
public class Pool<T extends PoolObject<T>> {

    /** List containing all pooled objects. */
    ArrayList<T> items;

    /** Queue of available indices that have been released back to the pool. */
    PriorityQueue<Integer> available;

    /** High-water mark index of unallocated items. */
    int lastIndex = 0;

    /** Maximum capacity of the pool. */
    int capacity;

    /**
     * Constructs a pool with a specified capacity and template element.
     *
     * @param _capacity maximum number of elements in the pool
     * @param elem prototype element used to populate the pool
     */
    public Pool(int _capacity, T elem) {
        capacity = _capacity;
        items = new ArrayList<>(capacity);
        available = new PriorityQueue<>();
        fill(elem);
    }

    /**
     * Populates the pool by cloning the prototype element up to capacity.
     *
     * @param elem prototype element to clone
     */
    public void fill(T elem) {
        for (int i = 0; i < capacity; ++i) {
            T temp = elem.clone();
            temp.idx = i;
            items.add(i, temp);
        }
    }

    /**
     * Fetches an available object from the pool.
     *
     * @return pooled object instance
     * @throws IndexOutOfBoundsException if the pool is fully depleted
     */
    public T fetchItem() {
        if (available.isEmpty()) {
            if (lastIndex == capacity) {
                throw new IndexOutOfBoundsException("Pool Depleted");
            }
            return items.get(lastIndex++);
        } else {
            return items.get(available.poll());
        }
    }

    /**
     * Releases an object back to the pool for reuse.
     *
     * @param item pooled object to release
     */
    public void releaseItem(T item) {
        available.add(item.idx);
    }
}
