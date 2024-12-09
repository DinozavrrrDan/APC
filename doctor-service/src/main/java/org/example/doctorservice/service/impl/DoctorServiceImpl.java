package org.example.doctorservice.service.impl;


import org.example.doctorservice.model.PatientRequest;
import org.example.doctorservice.service.DoctorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final RestTemplate restTemplate;

    private ExecutorService doctorPool;
    private AtomicInteger availableDoctors;
    private final CopyOnWriteArrayList<PatientRequest> statistic;
    private final static Random random = new Random();
    private final AtomicBoolean stepModeEnabled = new AtomicBoolean(false);

    public DoctorServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.doctorPool = Executors.newFixedThreadPool(5);
        this.availableDoctors = new AtomicInteger(5);
        this.statistic = new CopyOnWriteArrayList<>();
    }
    @Override
    public void setDoctors(int doctors) {
        System.out.println("Doctors set: " + doctors);
        this.doctorPool = Executors.newFixedThreadPool(doctors);
        this.availableDoctors = new AtomicInteger(doctors);
    }

    @Override
    public void handlePatient(PatientRequest patientRequest) {
        try {
            System.out.println("Doctor started treating patient: " + patientRequest.getPatient().getId());
            double minServiceTime = 1.5; // минимальное время обслуживания в секундах
            double maxServiceTime = 2.5; // максимальное время обслуживания в секундах
            // Генерируем время обслуживания в соответствии с равномерным распределением [minServiceTime, maxServiceTime]
            double serviceTime = minServiceTime + random.nextDouble() * (maxServiceTime - minServiceTime);
            Thread.sleep((long) (serviceTime * 10));

            System.out.println("Doctor finished treating patient: " + patientRequest.getPatient().getId());
            patientRequest.setTreated(true);
            if (stepModeEnabled.get()) {
                sendStepLogToStatistic(patientRequest);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendStepLogToStatistic(PatientRequest patientRequest){
        String statisticUrl =  "http://localhost:8089/statistic/step";
        String body = new Date() + " : Doctor finished treating patient: " + patientRequest.toString();
        restTemplate.postForObject(statisticUrl,
                body, String.class);

    }

    public void treatPatient(PatientRequest patientRequest) {
        availableDoctors.decrementAndGet();
        doctorPool.submit(() -> {
            handlePatient(patientRequest);
            patientRequest.setDoctorId(Thread.currentThread().threadId());
            statistic.add(patientRequest);
            System.out.println(availableDoctors.incrementAndGet());
        });
    }

    @Override
    public boolean isFreeDoctors(){
        return availableDoctors.get() > 0;
    }

    @Override
    public CopyOnWriteArrayList<PatientRequest> getStatistic() {
        return statistic;
    }

    @Override
    public void setStepMode(boolean stepMode) {
        stepModeEnabled.set(stepMode);
    }
}
