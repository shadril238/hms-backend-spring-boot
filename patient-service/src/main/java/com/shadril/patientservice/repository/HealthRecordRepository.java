package com.shadril.patientservice.repository;

import com.shadril.patientservice.entity.HealthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {
}
