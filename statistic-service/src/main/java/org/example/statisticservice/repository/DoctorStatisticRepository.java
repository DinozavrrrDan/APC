package org.example.statisticservice.repository;

import org.example.statisticservice.model.PatientRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorStatisticRepository extends JpaRepository<PatientRequestEntity,Long> {

}
