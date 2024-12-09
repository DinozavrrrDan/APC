package org.example.queueservice.service;

public interface Buffer<T> {
    boolean add(T item);
    T remove();
    int getSize();
    int getCapacity();
    Object[] getPatientRequests();
    String toString();
}
