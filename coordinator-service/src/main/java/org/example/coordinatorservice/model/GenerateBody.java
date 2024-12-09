package org.example.coordinatorservice.model;

public class GenerateBody {
    int numberOfPatients;
    int numberOfGenerators;

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

    public GenerateBody(int numberOfPatients, int numberOfGenerators) {
        this.numberOfPatients = numberOfPatients;
        this.numberOfGenerators = numberOfGenerators;
    }
}
