package org.example.coordinatorservice.model;

public class Settings {
    private int numberOfDoctors;
    private int numberOfBuffers;
    private int capacityOfBuffers;
    private int numberOfPatients;
    private int numberOfGenerators;
    private boolean step;

    public boolean isStep() {
        return step;
    }

    public void setStep(boolean step) {
        this.step = step;
    }

    public int getNumberOfDoctors() {
        return numberOfDoctors;
    }

    public void setNumberOfDoctors(int numberOfDoctors) {
        this.numberOfDoctors = numberOfDoctors;
    }

    public int getNumberOfBuffers() {
        return numberOfBuffers;
    }

    public void setNumberOfBuffers(int numberOfBuffers) {
        this.numberOfBuffers = numberOfBuffers;
    }

    public int getCapacityOfBuffers() {
        return capacityOfBuffers;
    }

    public void setCapacityOfBuffers(int capacityOfBuffers) {
        this.capacityOfBuffers = capacityOfBuffers;
    }

    public int getNumberOfPatients() {
        return numberOfPatients;
    }

    public void setNumberOfPatients(int numberOfPatients) {
        this.numberOfPatients = numberOfPatients;
    }

    public int getNumberOfGenerators() {
        return numberOfGenerators;
    }

    public void setNumberOfGenerators(int numberOfGenerators) {
        this.numberOfGenerators = numberOfGenerators;
    }
}
