package com.shadril.cdssservice.entity;

import com.shadril.cdssservice.enums.RecommendationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "health_recommendations")
public class HealthRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "recommendation_type")
    @Enumerated(EnumType.STRING)
    private RecommendationType recommendationType;

    @Column(name = "recommendation")
    private String recommendation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}