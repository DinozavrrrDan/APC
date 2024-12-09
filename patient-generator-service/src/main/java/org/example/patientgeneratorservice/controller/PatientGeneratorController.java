package org.example.patientgeneratorservice.controller;

import org.apache.coyote.Response;
import org.example.patientgeneratorservice.model.GenerateBody;
import org.example.patientgeneratorservice.model.GenerateSettings;
import org.example.patientgeneratorservice.model.PatientRequest;
import org.example.patientgeneratorservice.service.PatientRequestGeneratorService;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generator")
public class PatientGeneratorController {
    private final PatientRequestGeneratorService patientRequestGeneratorService;

    public PatientGeneratorController(PatientRequestGeneratorService patientRequestGeneratorService) {
        this.patientRequestGeneratorService = patientRequestGeneratorService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startPatientGenerator(@RequestBody GenerateSettings generateSettings) {
//        System.out.println(generateBody.getNumberOfPatients());
        patientRequestGeneratorService.generateAllPatients(generateSettings);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step")
    public ResponseEntity<?> stepMode(@RequestBody Boolean stepMode) {
//        System.out.println(generateBody.getNumberOfPatients());
        patientRequestGeneratorService.setStepMode(stepMode);
        return ResponseEntity.ok().build();
    }
}
