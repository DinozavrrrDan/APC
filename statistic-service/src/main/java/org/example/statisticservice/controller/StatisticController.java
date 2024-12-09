package org.example.statisticservice.controller;

import org.apache.coyote.Response;
import org.example.statisticservice.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<?> statistic() {
        return ResponseEntity.ok(statisticService.getStatistic());
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalStatistic() {
        return ResponseEntity.ok(statisticService.getTotalStatistics());
    }

    @PostMapping("/start")
    public ResponseEntity<?> start() {
        statisticService.startStatistic();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step")
    public ResponseEntity<?> step(@RequestBody String step) {
        statisticService.stepLog(step);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> delete() {
        statisticService.clearStatistic();
        return ResponseEntity.ok().build();
    }
}
