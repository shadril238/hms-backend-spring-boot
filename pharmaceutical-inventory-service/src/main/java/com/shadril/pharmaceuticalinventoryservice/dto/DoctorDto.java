package com.shadril.pharmaceuticalinventoryservice.dto;

import com.shadril.pharmaceuticalinventoryservice.enums.BloodGroup;
import com.shadril.pharmaceuticalinventoryservice.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private String doctorId;

    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    private String careerTitle;

    private String department;

    private String specialization;

    private String designation;

    private String institute;

    private Integer experienceYears;

    private String qualifications;

    private String licenseNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private String phoneNumber;

    private String biography;

    private String address;

    private boolean isApproved;

    private boolean isActive;
}
