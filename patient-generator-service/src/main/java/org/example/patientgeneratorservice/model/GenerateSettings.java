package org.example.patientgeneratorservice.model;

public class GenerateSettings {
    private int numberOfPatients;
    private int numberOfGenerators;

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
