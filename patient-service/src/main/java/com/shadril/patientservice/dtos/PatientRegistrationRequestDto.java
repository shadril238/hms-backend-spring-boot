package com.shadril.patientservice.dtos;

import com.shadril.patientservice.enums.BloodGroup;
import com.shadril.patientservice.enums.Gender;
import jakarta.persistence.Lob;
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
public class PatientRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @NotNull
    private BloodGroup bloodGroup;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotNull
    private boolean isApproved;

    @NotNull
    private boolean isActive;
}
