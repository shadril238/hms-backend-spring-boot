package com.shadril.pharmaceuticalinventoryservice.dto;

import com.shadril.pharmaceuticalinventoryservice.enums.EquipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalEquipmentDto {
    private Long medicalEquipmentId;
    private String medicalEquipmentName;
    private EquipmentType medicalEquipmentType;
    private String manufacturer;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private Boolean isOccupied;
    private Boolean isActive;
}
