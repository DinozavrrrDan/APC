package org.example.statisticservice.service.impl;

import org.example.statisticservice.model.PatientRequest;
import org.example.statisticservice.model.PatientRequestEntity;
import org.example.statisticservice.repository.BufferRepository;
import org.example.statisticservice.repository.DoctorStatisticRepository;
import org.example.statisticservice.repository.PatientRequestRepository;
import org.example.statisticservice.repository.TotalStatisticRepository;
import org.example.statisticservice.service.StatisticService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final RestTemplate restTemplate;
    private final PatientRequestRepository patientRequestRepository;
    private final TotalStatisticRepository totalStatisticRepository;

    public StatisticServiceImpl(RestTemplate restTemplate, PatientRequestRepository patientRequestRepository, TotalStatisticRepository totalStatisticRepository/* DoctorStatisticRepository doctorStatisticRepository, BufferRepository bufferRepository*/) {
        this.restTemplate = restTemplate;
        this.patientRequestRepository = patientRequestRepository;
        this.totalStatisticRepository = totalStatisticRepository;
    }

    @Override
    public List<PatientRequestEntity>  getStatistic() {
        return patientRequestRepository.findAll();
    }

    @Override
    public void startStatistic() {
        patientRequestRepository.saveAll(getTreatPatients());
        patientRequestRepository.saveAll(getRejectedPatients());
    }

    private  List<PatientRequestEntity> getTreatPatients(){
        String doctorServiceUrl = "http://localhost:8088/doctor/statistic";
        return getPatientRequestEntities(doctorServiceUrl);
    }

    private List<PatientRequestEntity> getPatientRequestEntities(String doctorServiceUrl) {
        ResponseEntity<List<PatientRequest>> responseEntity = restTemplate.exchange(
                doctorServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PatientRequest>>() {}
        );
        List<PatientRequest> patientRequests = responseEntity.getBody();
        List<PatientRequestEntity> patientRequestEntities = new ArrayList<>();
        System.out.println(patientRequests.getClass());
        patientRequests.forEach(patientRequest -> {
            patientRequestEntities.add(
                    new PatientRequestEntity(patientRequest)
            );
        });
        return patientRequestEntities;
    }

    private List<PatientRequestEntity> getRejectedPatients(){
        String queueServiceUrl = "http://localhost:8087/queue/statistic";
        return getPatientRequestEntities(queueServiceUrl);
    }
    private List<PatientRequestEntity> getBuffers(){
        String queueServiceUrl = "http://localhost:8087/queue/buffers";
        return getPatientRequestEntities(queueServiceUrl);
    }

    @Override
    public void clearStatistic() {
        patientRequestRepository.truncate();
    }

    @Override
    public void enableSteps() {
        String queueServiceUrl = "http://localhost:8087/queue/statistic";

    }


    public String getTotalStatistics() {
        StringBuilder stats = new StringBuilder();

        // 1. Среднее время ожидания
        Double avgWaitTime = totalStatisticRepository.findAverageWaitTime();
        stats.append("Среднее время ожидания в системе: ")
                .append(avgWaitTime != null ? String.format("%.2f секунд", avgWaitTime) : "Нет данных")
                .append("\n");

        // 2. Количество отказов по генераторам
        List<Object[]> failuresByGenerator = totalStatisticRepository.findFailuresByGenerator();
        Map<Long, Long> failuresByGeneratorMap = failuresByGenerator.stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        List<Object[]> requestsByGenerator = totalStatisticRepository.findRequestsByGenerator();
        stats.append("Количество отказов и процент отказов по генераторам:\n");
        if (!requestsByGenerator.isEmpty()) {
            for (Object[] row : requestsByGenerator) {
                Long generatorId = (Long) row[0];
                Long totalRequests = (Long) row[1];
                Long failures = failuresByGeneratorMap.getOrDefault(generatorId, 0L);
                double failureRate = (totalRequests > 0) ? (failures * 100.0 / totalRequests) : 0.0;

                stats.append("  Генератор ").append(generatorId)
                        .append(": ").append(failures).append(" отказов, ")
                        .append(String.format("%.2f%% отказов", failureRate)).append("\n");
            }
        } else {
            stats.append("  Нет данных\n");
        }

// 3. Количество отказов по буферам
        List<Object[]> failuresByBuffer = totalStatisticRepository.findFailuresByBuffer();
        Map<Long, Long> failuresByBufferMap = failuresByBuffer.stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        List<Object[]> requestsByBuffer = totalStatisticRepository.findRequestsByBuffer();
        stats.append("Количество отказов и процент отказов по буферам:\n");
        if (!requestsByBuffer.isEmpty()) {
            for (Object[] row : requestsByBuffer) {
                Long bufferId = (Long) row[0];
                Long totalRequests = (Long) row[1];
                Long failures = failuresByBufferMap.getOrDefault(bufferId, 0L);
                double failureRate = (totalRequests > 0) ? (failures * 100.0 / totalRequests) : 0.0;

                stats.append("  Буфер ").append(bufferId)
                        .append(": ").append(failures).append(" отказов, ")
                        .append(String.format("%.2f%% отказов", failureRate)).append("\n");
            }
        } else {
            stats.append("  Нет данных\n");
        }


        // 4. Количество заявок у каждого доктора
        List<Object[]> requestsByDoctor = totalStatisticRepository.findRequestsByDoctor();
        stats.append("Количество заявок у каждого доктора:\n");
        if (!requestsByDoctor.isEmpty()) {
            for (Object[] row : requestsByDoctor) {
                stats.append("  Доктор ").append(row[0]).append(": ").append(row[1]).append(" заявок\n");
            }
        } else {
            stats.append("  Нет данных\n");
        }

        // 5. Количество заявок у каждого буфера
        requestsByBuffer = totalStatisticRepository.findRequestsByBuffer();
        stats.append("Количество заявок у каждого буфера:\n");
        if (!requestsByBuffer.isEmpty()) {
            for (Object[] row : requestsByBuffer) {
                stats.append("  Буфер ").append(row[0]).append(": ").append(row[1]).append(" заявок\n");
            }
        } else {
            stats.append("  Нет данных\n");
        }

        // 6. Количество заявок у каждого генератора
        requestsByGenerator = totalStatisticRepository.findRequestsByGenerator();
        stats.append("Количество заявок у каждого генератора:\n");
        if (!requestsByGenerator.isEmpty()) {
            for (Object[] row : requestsByGenerator) {
                stats.append("  Генератор ").append(row[0]).append(": ").append(row[1]).append(" заявок\n");
            }
        } else {
            stats.append("  Нет данных\n");
        }

        // 7. Общая вероятность отказа
        Double failureProbability = totalStatisticRepository.findFailureProbability();
        stats.append("Общая вероятность отказа: ")
                .append(failureProbability != null ? String.format("%.2f%%", failureProbability * 100) : "Нет данных")
                .append("\n");

        // 8. Максимальное время нахождения в системе
        Long maxWaitTime = totalStatisticRepository.findMaxWaitTime();
        stats.append("Максимальное время нахождения в системе: ")
                .append(maxWaitTime != null ? maxWaitTime + " секунд" : "Нет данных")
                .append("\n");

        // 9. Минимальное время нахождения в системе
        Long minWaitTime = totalStatisticRepository.findMinWaitTime();
        stats.append("Минимальное время нахождения в системе: ")
                .append(minWaitTime != null ? minWaitTime + " секунд" : "Нет данных")
                .append("\n");

        return stats.toString();
    }

    @Override
    public void stepLog(String message) {
        System.out.println(message);
    }
}
