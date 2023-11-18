package com.shadril.pharmaceuticalinventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_equipment_allocation")
public class MedicalEquipmentAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentAllocationId;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_equipment_id", nullable = false)
    private MedicalEquipmentEntity medicalEquipment;

    @Column(nullable = false)
    private Boolean isReturned = false;

    @Column(nullable = false)
    private LocalDate allocatedDate;

    @Column(nullable = true)
    private LocalDate returnedDate;

    @Column(nullable = false)
    private Boolean isActive = true;
}
