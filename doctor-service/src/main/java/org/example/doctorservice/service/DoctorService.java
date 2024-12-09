package org.example.doctorservice.service;


import org.example.doctorservice.model.PatientRequest;

import java.util.concurrent.CopyOnWriteArrayList;

public interface DoctorService {

    void handlePatient(PatientRequest patientRequest);
    void treatPatient(PatientRequest patientRequest);
    boolean isFreeDoctors();
    CopyOnWriteArrayList<PatientRequest> getStatistic();
    void setDoctors(int doctors);
    void setStepMode(boolean stepMode);
}
