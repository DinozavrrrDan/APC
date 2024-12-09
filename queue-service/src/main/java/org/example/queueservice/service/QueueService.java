package org.example.queueservice.service;


import org.example.queueservice.model.PatientRequest;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public interface QueueService {
    void addPatientToQueue(PatientRequest patientRequest);
    PatientRequest getNextPatient();
    CopyOnWriteArrayList<PatientRequest> getStatistic();
    Map<Integer, Buffer<PatientRequest>> getBufferStatistic();
    void setBufferNumber(int bufferNumber);
    void setBufferCapacity(int bufferCapacity);
    void regenerateBuffers();
    int getCounter();
    void setStepMode(boolean stepMode);
}
