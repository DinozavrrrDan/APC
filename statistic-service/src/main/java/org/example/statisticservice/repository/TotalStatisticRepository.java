package org.example.statisticservice.repository;

import org.example.statisticservice.model.PatientRequestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TotalStatisticRepository  extends CrudRepository<PatientRequestEntity, Long> {

    // 1. Среднее время ожидания в системе (waitTime)
    @Query(value = "SELECT AVG(wait_time) FROM patient_request_entity WHERE wait_time IS NOT NULL", nativeQuery = true)
    Double findAverageWaitTime();

    // 2. Количество отказов (isTreated = false) у каждого генератора (generatorId)
    @Query(value = "SELECT generator_id, COUNT(*) FROM patient_request_entity WHERE is_treated = false AND generator_id IS NOT NULL GROUP BY generator_id", nativeQuery = true)
    List<Object[]> findFailuresByGenerator();

    // 3. Количество отказов (isTreated = false) у каждого буфера (bufferId)
    @Query(value = "SELECT buffer_id, COUNT(*) FROM patient_request_entity WHERE is_treated = false AND buffer_id IS NOT NULL GROUP BY buffer_id", nativeQuery = true)
    List<Object[]> findFailuresByBuffer();

    // 4. Количество заявок у каждого доктора (doctorId)
    @Query(value = "SELECT doctor_id, COUNT(*) FROM patient_request_entity WHERE doctor_id IS NOT NULL GROUP BY doctor_id", nativeQuery = true)
    List<Object[]> findRequestsByDoctor();

    // 5. Количество заявок у каждого буфера (bufferId)
    @Query(value = "SELECT buffer_id, COUNT(*) FROM patient_request_entity WHERE buffer_id IS NOT NULL GROUP BY buffer_id", nativeQuery = true)
    List<Object[]> findRequestsByBuffer();

    // 6. Количество заявок у каждого генератора (generatorId)
    @Query(value = "SELECT generator_id, COUNT(*) FROM patient_request_entity WHERE generator_id IS NOT NULL GROUP BY generator_id", nativeQuery = true)
    List<Object[]> findRequestsByGenerator();

    // 7. Общая вероятность отказа (isTreated = false / общее количество заявок)
    @Query(value = "SELECT (COUNT(CASE WHEN is_treated = false THEN 1 END) * 1.0 / COUNT(*)) FROM patient_request_entity", nativeQuery = true)
    Double findFailureProbability();

    // 8. Максимальное время нахождения в системе
    @Query(value = "SELECT MAX(wait_time) FROM patient_request_entity WHERE wait_time IS NOT NULL", nativeQuery = true)
    Long findMaxWaitTime();

    // 9. Минимальное время нахождения в системе
    @Query(value = "SELECT MIN(wait_time) FROM patient_request_entity WHERE wait_time IS NOT NULL", nativeQuery = true)
    Long findMinWaitTime();
}
