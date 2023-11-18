package com.shadril.pharmaceuticalinventoryservice.entity;

import com.shadril.pharmaceuticalinventoryservice.enums.MedicineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MedicineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    @Column(nullable = false, name = "medicine_name")
    private String medicineName;

    @Column(nullable = false, name = "generic_name")
    private String genericName;

    @Column(nullable = false, name = "medicine_type")
    @Enumerated(EnumType.STRING)
    private MedicineType medicineType;

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
