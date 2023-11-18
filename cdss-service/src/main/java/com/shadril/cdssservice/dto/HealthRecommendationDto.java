package com.shadril.cdssservice.dto;

import com.shadril.cdssservice.enums.RecommendationType;

import java.time.LocalDateTime;

public class HealthRecommendationDto {
    private String patientId;
    private String recommendation;
    private RecommendationType recommendationType;
    private LocalDateTime createdAt;

}
