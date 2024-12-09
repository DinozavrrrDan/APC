package org.example.queueservice.service.impl;


import org.example.queueservice.service.Buffer;

import java.util.Arrays;

public class CircularBuffer<T> implements Buffer<T> {
    private Object[] buffer;
    private int head;
    private int tail;
    private int size;
    private int capacity;

    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    @Override
    public boolean add(T item) {
        if (size == capacity) {
            System.out.println("Buffer is full. Overwriting oldest element.");
            head = (head + 1) % capacity;
        } else {
            size++;
        }
        System.out.println("Adding " + item + " to buffer");
        buffer[tail] = item;
        tail = (tail + 1) % capacity;
        return true;
    }

    @Override
    public T remove() {
        if (size == 0) {
//            System.out.println("Buffer is empty. Cannot remove element.");
            return null;
        }
        @SuppressWarnings("unchecked")
        T item = (T) buffer[head];
        buffer[head] = null;
        head = (head + 1) % capacity;
        size--;
        System.out.println("Remove " + item + " to buffer");
        return item;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Object[] getPatientRequests() {
        return buffer;
    }

    @Override
    public String toString() {
        return "{Buffer " +
                "buffer = " + Arrays.deepToString(buffer)
                + ", size = " + size
                + ", capacity = " + capacity
                + "}";
    }
}
