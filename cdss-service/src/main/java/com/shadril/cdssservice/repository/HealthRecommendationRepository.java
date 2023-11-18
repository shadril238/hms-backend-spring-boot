package com.shadril.cdssservice.repository;

import com.shadril.cdssservice.dto.HealthRecommendationDto;
import com.shadril.cdssservice.entity.HealthRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecommendationRepository extends JpaRepository<HealthRecommendationEntity, Long> {
    List<HealthRecommendationEntity> findAllByPatientId(String patientId);
}
