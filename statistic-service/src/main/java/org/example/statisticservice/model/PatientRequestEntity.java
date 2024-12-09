package org.example.statisticservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class PatientRequestEntity {
    @Id
    private Long Id;
    private String name;
    private String contactInfo;
    private Date requestTime;
    private Long waitTime = null;
    private Date requestTimeInBuffer = new Date();
    private boolean isTreated = false;
    private Long generatorId = null;
    private Long doctorId = null;
    private Long bufferId = null;

    public PatientRequestEntity(PatientRequest patientRequest) {
        this.Id = (long) patientRequest.getPatient().getId();
        this.name = patientRequest.getPatient().getName();
        this.contactInfo = patientRequest.getPatient().getContactInfo();
        this.requestTime = patientRequest.getRequestTime();
        this.waitTime = patientRequest.getWaitTime();
        this.requestTimeInBuffer = patientRequest.getRequestTimeInBuffer();
        this.isTreated =patientRequest.isTreated();
        this.generatorId = patientRequest.getGeneratorId();
        this.doctorId = patientRequest.getDoctorId();
        this.bufferId = patientRequest.getBufferId();
    }

    public PatientRequestEntity() {
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

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
}