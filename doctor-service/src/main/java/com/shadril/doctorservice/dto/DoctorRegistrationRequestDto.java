package com.shadril.doctorservice.dto;

import com.shadril.doctorservice.enums.BloodGroup;
import com.shadril.doctorservice.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRegistrationRequestDto {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Career title is required")
    private String careerTitle;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Institute is required")
    private String institute;

    @NotBlank(message = "Experience years is required")
    private Integer experienceYears;

    @NotBlank(message = "Qualifications is required")
    private String qualifications;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Biography is required")
    private String biography;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull
    private boolean isApproved;

    @NotNull
    private boolean isActive;
}
