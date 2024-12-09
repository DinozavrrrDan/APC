package org.example.coordinatorservice.model;

public class BufferSettings {
    private int bufferNumber;
    private int bufferCapacity;

    public int getBufferNumber() {
        return bufferNumber;
    }

    public void setBufferNumber(int bufferNumber) {
        this.bufferNumber = bufferNumber;
    }

    public int getBufferCapacity() {
        return bufferCapacity;
    }

    public void setBufferCapacity(int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
    }

    public BufferSettings(int bufferNumber, int bufferCapacity) {
        this.bufferNumber = bufferNumber;
        this.bufferCapacity = bufferCapacity;
    }
}
