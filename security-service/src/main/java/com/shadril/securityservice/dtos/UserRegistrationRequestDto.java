package com.shadril.securityservice.dtos;

import com.shadril.securityservice.enums.BloodGroup;
import com.shadril.securityservice.enums.Gender;
import com.shadril.securityservice.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequestDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    private String phoneNumber;
    @Lob // Large object for storing large string
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isActive;
}
