package com.shadril.patientservice.entities;

import com.shadril.patientservice.enums.BloodGroup;
import com.shadril.patientservice.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient-id-generator")
    private String patientId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    @Lob // Large object for storing large string
    private String address;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false)
    private boolean isActive;
}
