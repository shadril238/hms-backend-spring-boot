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
@Table(name = "medicine_allocation")
public class MedicineAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineAllocationId;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private MedicineEntity medicine;

    @Column(nullable = false)
    private LocalDate allocatedDate;

    @Column(nullable = true)
    private LocalDate returnedDate;

    @Column(nullable = false)
    private Boolean isReturned = false;

    @Column(nullable = false)
    private Boolean isActive = true;
}
