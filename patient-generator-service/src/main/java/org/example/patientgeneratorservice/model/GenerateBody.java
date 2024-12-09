package org.example.patientgeneratorservice.model;

public class GenerateBody {
    int numberOfPatients;

    public int getNumberOfPatients() {
        return numberOfPatients;
    }

    public void setNumberOfPatients(int numberOfPatients) {
        this.numberOfPatients = numberOfPatients;
    }

    public GenerateBody(int numberOfPatients) {
        this.numberOfPatients = numberOfPatients;
    }
}
