package com.shadril.pharmaceuticalinventoryservice.entity;

import com.shadril.pharmaceuticalinventoryservice.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_equipment")
public class MedicalEquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalEquipmentId;

    @Column(nullable = false, name = "medical_equipment_name")
    private String medicalEquipmentName;

    @Column(nullable = false, name = "medical_equipment_type")
    @Enumerated(EnumType.STRING)
    private EquipmentType medicalEquipmentType;

    @Column(nullable = false, name = "manufacturer")
    private String manufacturer;

    @Column(nullable = false, name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(nullable = false, name = "expiry_date")
    private LocalDate expiryDate;

    @Column(nullable = false, name = "is_occupied")
    private Boolean isOccupied = false;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive = true;
}
