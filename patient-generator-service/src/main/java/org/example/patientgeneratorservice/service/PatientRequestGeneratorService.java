package org.example.patientgeneratorservice.service;

import org.example.patientgeneratorservice.model.GenerateSettings;
import org.example.patientgeneratorservice.model.PatientRequest;

public interface PatientRequestGeneratorService {
    void generateAllPatients(GenerateSettings generateSettings);
    void setStepMode(boolean stepMode);
}
