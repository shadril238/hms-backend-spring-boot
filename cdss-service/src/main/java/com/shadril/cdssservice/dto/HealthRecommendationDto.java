package com.shadril.cdssservice.dto;

import com.shadril.cdssservice.enums.RecommendationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthRecommendationDto {
    private String patientId;
    private String recommendation;
    private RecommendationType recommendationType;
    private LocalDateTime createdAt;

}
