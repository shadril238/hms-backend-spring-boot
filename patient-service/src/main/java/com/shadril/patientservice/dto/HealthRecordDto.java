package com.shadril.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecordDto {
    private String patientId;
    private LocalDate checkupDate;
    private Integer heightInCm;
    private Integer weightInKg;
    private String bloodPressure;
    private String bloodSugar;
    private String bodyTemperature;
    private String pulseRate;
    private String allergies;
    private String pastSurgeries;
    private Boolean hasDiabetes;
    private Boolean isHypertensive;
    private Boolean hasHeartDisease;
    private Boolean hasKidneyDisease;
    private Boolean hasLiverDisease;
    private Boolean hasCancer;
    private Boolean hasHiv;
    private Boolean hasTb;
    private String physicalDisability;
    private String vaccineInfo;
    private Boolean isSmoker;
    private Boolean isAlcoholic;
    private Boolean isActive;
}

