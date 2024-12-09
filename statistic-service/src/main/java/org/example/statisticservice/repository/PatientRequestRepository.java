package org.example.statisticservice.repository;

import org.example.statisticservice.model.PatientRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRequestRepository extends JpaRepository<PatientRequestEntity,Long> {

    @NativeQuery("TRUNCATE TABLE patient_request_entity")
    void truncate();
}
