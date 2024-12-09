package org.example.queueservice.controller;

import org.example.queueservice.model.BufferSettings;
import org.example.queueservice.model.PatientRequest;
import org.example.queueservice.service.Buffer;
import org.example.queueservice.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPatient(@RequestBody PatientRequest patientRequest) {
        System.out.println("addPatient" + patientRequest);
        queueService.addPatientToQueue(patientRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/patient")
    public ResponseEntity<PatientRequest> getNextPatient() {
        return new ResponseEntity<>(queueService.getNextPatient(), HttpStatus.OK);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<PatientRequest>> statistic() {
        return new ResponseEntity<>(queueService.getStatistic(), HttpStatus.OK);
    }

    @GetMapping("/statistic/total/patients")
    public ResponseEntity<Integer> statisticTotalPatients() {
        return new ResponseEntity<>(queueService.getCounter(), HttpStatus.OK);
    }

    @GetMapping("/buffers")
    public ResponseEntity<Map<Integer, Buffer<PatientRequest>>> buffersStatistic() {
        return new ResponseEntity<>(queueService.getBufferStatistic(), HttpStatus.OK);
    }

    @PostMapping("/settings")
    public ResponseEntity<?> settings(@RequestBody BufferSettings bufferSettings) {
        queueService.setBufferNumber(bufferSettings.getBufferNumber());
        queueService.setBufferCapacity(bufferSettings.getBufferCapacity());
        queueService.regenerateBuffers();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step")
    public ResponseEntity<?> step(@RequestBody Boolean stepMode) {
        queueService.setStepMode(stepMode);
        return ResponseEntity.ok().build();
    }
}
