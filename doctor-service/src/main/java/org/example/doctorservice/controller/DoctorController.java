package org.example.doctorservice.controller;

import org.example.doctorservice.model.PatientRequest;
import org.example.doctorservice.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("doctor")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/treat")
    public ResponseEntity<?> treatPatient(@RequestBody PatientRequest patientRequest) {
        doctorService.treatPatient(patientRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/free")
    public ResponseEntity<?> isFreeDoctors() {
        return ResponseEntity.ok(doctorService.isFreeDoctors());
    }

    @GetMapping("/statistic")
    public ResponseEntity<CopyOnWriteArrayList<PatientRequest>> statistic() {
        return ResponseEntity.ok(doctorService.getStatistic());
    }

    @PostMapping("/settings")
    public ResponseEntity<?> settings(@RequestBody Integer doctors) {
        doctorService.setDoctors(doctors);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step")
    public ResponseEntity<?> stepMode(@RequestBody Boolean stepMode) {
        doctorService.setStepMode(stepMode);
        return ResponseEntity.ok().build();
    }
}
