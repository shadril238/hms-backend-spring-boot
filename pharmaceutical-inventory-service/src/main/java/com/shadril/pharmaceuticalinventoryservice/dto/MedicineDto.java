package com.shadril.pharmaceuticalinventoryservice.dto;

import com.shadril.pharmaceuticalinventoryservice.enums.MedicineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineDto {
    private Long medicineId;
    private String medicineName;
    private String genericName;
    private MedicineType medicineType;
    private String manufacturer;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private Boolean isOccupied;
    private Boolean isActive;
}
