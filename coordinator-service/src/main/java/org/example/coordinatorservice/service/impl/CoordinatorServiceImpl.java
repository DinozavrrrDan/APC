package org.example.coordinatorservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coordinatorservice.model.BufferSettings;
import org.example.coordinatorservice.model.GenerateBody;
import org.example.coordinatorservice.model.PatientRequest;
import org.example.coordinatorservice.model.Settings;
import org.example.coordinatorservice.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CoordinatorServiceImpl implements CoordinatorService {
    private final RestTemplate restTemplate;
    private final RestTemplate restTemplate2 = new RestTemplate();
    private Integer numberOfPatients = 0;
    private final AtomicBoolean isFinishedGeneration = new AtomicBoolean(false);

    public CoordinatorServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processPatients(Settings settings) {
        if (settings.isStep()){
            setStepMode();
        }

        setServices(settings);
        startGenerate(settings);

        numberOfPatients = settings.getNumberOfPatients();
        int counter = 0;
        boolean isFinished = false;
        while (!isFinished) {
            if (isFreeDoctors()) {
                PatientRequest patientRequest = getPatientRequest();
                if (patientRequest != null) {
                    treatPatient(patientRequest);
                }
            }
            if (isFinishedGeneration.get()) {
                counter++;
                if (counter > 100) {
                    isFinished = true;
                }
            }
        }
        isFinishedGeneration.set(true);
    }

    @Override
    public void setNumberOfPatients(int numberOfPatients) {
        this.numberOfPatients = numberOfPatients;
        System.out.println(numberOfPatients + " patients set");
    }

    public void setServices(Settings settings) {
        String doctorServiceUrl = "http://localhost:8088/doctor/settings";
        String queueServiceUrl = "http://localhost:8087/queue/settings";

        restTemplate.postForObject(
                doctorServiceUrl,
                settings.getNumberOfDoctors(),
                Integer.class);
        restTemplate.postForObject(
                queueServiceUrl,
                new BufferSettings(settings.getNumberOfBuffers(),
                        settings.getCapacityOfBuffers()), BufferSettings.class);

    }

    private void setStepMode(){
        String patientGeneratorServiceUrl = "http://localhost:8086/generator/step";
        restTemplate.postForObject(
                patientGeneratorServiceUrl,
                true,
                Boolean.class
        );
        String doctorServiceUrl = "http://localhost:8088/doctor/step";
        restTemplate.postForObject(
                doctorServiceUrl,
                true,
                Boolean.class
        );
        String queueServiceUrl = "http://localhost:8087/queue/step";
        restTemplate.postForObject(
                queueServiceUrl,
                true,
                Boolean.class
        );
    }

    private void startGenerate(Settings settings) {
        String patientGeneratorServiceUrl = "http://localhost:8086/generator/start";
        System.out.println("Generating " + settings.getNumberOfPatients() + " patients...");
        restTemplate.postForObject(patientGeneratorServiceUrl, new GenerateBody(settings.getNumberOfPatients(), settings.getNumberOfGenerators()), String.class);
    }

    private Boolean isFreeDoctors() {
        String doctorServiceUrl = "http://localhost:8088/doctor/free";
//        System.out.println("Checking doctors...");
        return restTemplate.getForObject(doctorServiceUrl, Boolean.class);
    }

    private PatientRequest getPatientRequest() {
        String queueServiceUrl = "http://localhost:8087/queue/patient";
//        System.out.println("Get patient request...");
        return restTemplate.getForObject(queueServiceUrl, PatientRequest.class);
    }

    private Integer getPatientsCounter() {
        String queueServiceUrl = "http://localhost:8087/queue/statistic/total/patients";
//        System.out.println("Get patient request...");
        Integer i = restTemplate2.getForObject("http://localhost:8087/queue/statistic/total/patients", Integer.class);
//        System.out.println(i);
        return i;
    }


    private void treatPatient(PatientRequest patientRequest) {
        String doctorServiceUrl = "http://localhost:8088/doctor/treat";
//        System.out.println("Treating patient...");
//        System.out.println(patientRequest.toString());
        restTemplate.postForObject(doctorServiceUrl, patientRequest, String.class);
    }

    @Override
    public Boolean getIsFinishedGeneration() {
        isFinishedGeneration.set(getPatientsCounter() > numberOfPatients);
        System.out.println(getPatientsCounter());
        System.out.println(numberOfPatients);
        return isFinishedGeneration.get();
    }

}
