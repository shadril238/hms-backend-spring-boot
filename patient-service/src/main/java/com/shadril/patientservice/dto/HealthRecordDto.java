package com.shadril.patientservice.dto;

import java.time.LocalDate;

public class HealthRecordDto {
    private Long patientId;
    private LocalDate checkupDate;
    private String checkupDoctorId;
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
    private Boolean isCancer;
    private Boolean hasHiv;
    private Boolean hasTb;
    private String physicalDisability;
    private String vaccineInfo;
    private Boolean isSmoker;
    private Boolean isAlcoholic;
    private Boolean isActive;
}

