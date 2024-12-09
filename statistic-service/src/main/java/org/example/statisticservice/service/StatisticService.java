package org.example.statisticservice.service;

import org.example.statisticservice.model.PatientRequestEntity;

import java.util.List;

public interface StatisticService {
    List<PatientRequestEntity> getStatistic();
    void startStatistic();
    void clearStatistic();
    void enableSteps();
    String getTotalStatistics();
    void stepLog(String step);
}
