package com.shadril.cdssservice.repository;

import com.shadril.cdssservice.entity.HealthRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecommendationRepository extends JpaRepository<HealthRecommendationEntity, Long> {
}
