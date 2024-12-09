package org.example.patientgeneratorservice.service.impl;

import org.example.patientgeneratorservice.model.GenerateSettings;
import org.example.patientgeneratorservice.model.Patient;
import org.example.patientgeneratorservice.model.PatientRequest;
import org.example.patientgeneratorservice.service.PatientRequestGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

@Service
public class PatientRequestGeneratorServiceImpl implements PatientRequestGeneratorService {

    //    private final Map<Integer, Generator> generators = new ConcurrentHashMap<>();
    private static final String[] NAMES = {"John", "Jane", "Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
    private static final String[] SURNAMES = {"Doe", "Smith", "Brown", "White", "Black", "Green", "Gray", "Blue"};
    private static final Random RANDOM = new Random();
    private static final double lambda = 0.1;
    private final AtomicInteger availableRequests;
    private final AtomicInteger nextId;
    private final RestTemplate restTemplate;
    private final AtomicBoolean stepModeEnabled = new AtomicBoolean(false);
    private ExecutorService patientGeneratorPool;

    public PatientRequestGeneratorServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.nextId = new AtomicInteger(0);
        this.patientGeneratorPool = Executors.newFixedThreadPool(4);
        this.availableRequests = new AtomicInteger(0);
    }

    @Override
    public void generateAllPatients(GenerateSettings generateSettings) {
//        start();
        this.patientGeneratorPool = Executors.newFixedThreadPool(generateSettings.getNumberOfGenerators());
        System.out.println("start" + generateSettings.getNumberOfPatients());
        availableRequests.set(generateSettings.getNumberOfPatients());

        for (int i = 0; i < generateSettings.getNumberOfGenerators(); i++) {
            int finalI = i;
            patientGeneratorPool.submit(() -> {
                generatePatients(finalI);
            });
        }
    }

    //    class Generator {
    private void generatePatients(long index) {
        while (availableRequests.get() > 0) {
            timeDelay();
            PatientRequest patientRequest = new PatientRequest(getRandomPatient());
            patientRequest.setGeneratorId(index);
            if (stepModeEnabled.get()) {
               sendStepLogToStatistic(patientRequest);
            }
            addPatientToQueue(patientRequest);
//            System.out.println("Patient request generated " + index);
//            System.out.println(currentThread().threadId());
            System.out.println(availableRequests.decrementAndGet());
        }
    }
//    }

    private synchronized void addPatientToQueue(PatientRequest patientRequest) {
        String queueServiceUrl = "http://localhost:8087/queue/add";
//        System.out.println("addPatientToQueue: " + queueServiceUrl);
        restTemplate.postForObject(queueServiceUrl, patientRequest, String.class);
//        System.out.println("end addPatientToQueue: " + queueServiceUrl);
    }

    private synchronized void start() {
        String queueServiceUrl = "http://localhost:8085/api/coordinator";
        restTemplate.postForObject(queueServiceUrl, null, String.class);
    }

    private synchronized Patient getRandomPatient() {
//        System.out.println("RANDOM PATIENT REQUEST GENERATED");
        String name = NAMES[RANDOM.nextInt(NAMES.length)] + " " + SURNAMES[RANDOM.nextInt(SURNAMES.length)];
        String contactInfo = "patient" + nextId + "@example.ru";
        return new Patient(nextId.incrementAndGet(), name, contactInfo);
    }

    private void timeDelay() {
        double interArrivalTime = -Math.log(1.0 - RANDOM.nextDouble()) / lambda;
//        System.out.println(interArrivalTime);
        try {
//            System.out.println("sleep " + interArrivalTime);
            Thread.sleep((long) (10 * interArrivalTime)); // Пуассоновский поток
//            System.out.println("end sleep " + interArrivalTime);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    private void sendStepLogToStatistic(PatientRequest patientRequest){
        String statisticUrl =  "http://localhost:8089/statistic/step";
        String body = new Date() + " : Generated patient: " + patientRequest.toString();
        restTemplate.postForObject(statisticUrl,
                body, String.class);

    }

    @Override
    public void setStepMode(boolean stepMode) {
        stepModeEnabled.set(stepMode);
    }
}
