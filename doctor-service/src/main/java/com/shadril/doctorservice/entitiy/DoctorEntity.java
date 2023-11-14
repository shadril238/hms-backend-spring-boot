package com.shadril.doctorservice.entitiy;

import com.shadril.doctorservice.enums.BloodGroup;
import com.shadril.doctorservice.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
public class DoctorEntity {
    @Id
    @GeneratedValue(generator = "doctor-id-generator")
    @GenericGenerator(
            name = "doctor-id-generator",
            strategy = "com.shadril.doctorservice.generator.DoctorIdGenerator"
    )
    private String doctorId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String careerTitle;

    @Column(nullable = false, length = 20)
    private String department;

    @Column(nullable = false, length = 20)
    private String specialization;

    @Column(nullable = false, length = 20)
    private String designation;

    @Column(nullable = false, length = 20)
    private String institute;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false)
    private String qualifications;

    @Column(nullable = false)
    private String licenseNumber;

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

    @Column(nullable = true, length = 500)
    @Lob
    private String biography;

    @Column(nullable = false)
    @Lob // Large object for storing large string
    private String address;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false)
    private boolean isActive;
}
