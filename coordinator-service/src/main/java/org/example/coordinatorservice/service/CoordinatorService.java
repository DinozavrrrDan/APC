package org.example.coordinatorservice.service;

import org.example.coordinatorservice.model.Settings;

public interface CoordinatorService {
    void processPatients(Settings settings);
    void setNumberOfPatients(int numberOfPatients);
    Boolean getIsFinishedGeneration();
}
