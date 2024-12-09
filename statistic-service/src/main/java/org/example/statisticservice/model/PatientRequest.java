package org.example.statisticservice.model;

import java.util.Date;

public class PatientRequest {
    private final Patient patient;
    private final Date requestTime;
    private Long waitTime = null;
    private Date requestTimeInBuffer = new Date();
    private boolean isTreated = false;
    private Long generatorId = null;
    private Long doctorId = null;
    private Long bufferId = null;
    public PatientRequest(Patient patient) {
        this.patient = patient;
        this.requestTime = new Date();
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }


    @Override
    public String toString() {
        return "PatientRequest{" +
                "patient=" + patient +
                ", requestTime=" + requestTime +
                '}';
    }

    public Date getRequestTimeInBuffer() {
        return requestTimeInBuffer;
    }

    public void setRequestTimeInBuffer(Date requestTimeInBuffer) {
        this.requestTimeInBuffer = requestTimeInBuffer;
    }

    public boolean isTreated() {
        return isTreated;
    }

    public void setTreated(boolean treated) {
        isTreated = treated;
    }

    public Long getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(Long generatorId) {
        this.generatorId = generatorId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getBufferId() {
        return bufferId;
    }

    public void setBufferId(Long bufferId) {
        this.bufferId = bufferId;
    }
}