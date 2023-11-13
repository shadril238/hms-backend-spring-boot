package com.shadril.patientservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "health_records")
public class HealthRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_record_id")
    private Long healthRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(nullable = false, name = "checkup_date")
    private LocalDate checkupDate;

    @Column(nullable = false, name = "height_in_cm")
    private Integer heightInCm;

    @Column(nullable = false, name = "weight_in_kg")
    private Integer weightInKg;

    @Column(nullable = false, name = "blood_pressure")
    private String bloodPressure;

    @Column(nullable = false, name = "blood_sugar")
    private String bloodSugar;

    @Column(nullable = false, name = "body_temperature")
    private String bodyTemperature;

    @Column(nullable = false, name = "pulse_rate")
    private String pulseRate;

    @Column(nullable = false, name = "allergies")
    private String allergies;

    @Column(nullable = false, name = "past_surgeries")
    private String pastSurgeries;

    @Column(nullable = false, name = "has_diabetes")
    private Boolean hasDiabetes;

    @Column(nullable = false, name = "is_hypertensive")
    private Boolean isHypertensive;

    @Column(nullable = false, name = "has_heart_disease")
    private Boolean hasHeartDisease;

    @Column(nullable = false, name = "has_kidney_disease")
    private Boolean hasKidneyDisease;

    @Column(nullable = false, name = "has_liver_disease")
    private Boolean hasLiverDisease;

    @Column(nullable = false, name = "has_cancer")
    private Boolean hasCancer;

    @Column(nullable = false, name = "has_hiv")
    private Boolean hasHiv;

    @Column(nullable = false, name = "has_tb")
    private Boolean hasTb;

    @Column(nullable = false, name = "physical_disability")
    private String physicalDisability;

    @Column(nullable = false, name = "vaccine_info")
    private String vaccineInfo;

    @Column(nullable = false, name = "is_smoker")
    private Boolean isSmoker;

    @Column(nullable = false, name = "is_alcoholic")
    private Boolean isAlcoholic;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;
}
