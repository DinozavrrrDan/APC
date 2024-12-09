package org.example.coordinatorservice.controller;

import org.example.coordinatorservice.model.Settings;
import org.example.coordinatorservice.service.CoordinatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coordinator")
public class CoordinatorController {
    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping
    public ResponseEntity<?> startCoordinator(@RequestBody Settings settings) {
        coordinatorService.processPatients(settings);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Boolean> isFinishedGeneration() {
        return ResponseEntity.ok(coordinatorService.getIsFinishedGeneration());
    }
}
