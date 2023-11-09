package com.shadril.securityservice.dtos;

import com.shadril.securityservice.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
//    private String password;
//    private String firstName;
//    private String lastName;
//    private LocalDate dateOfBirth;
//    @Enumerated(EnumType.STRING)
//    private Gender gender;
//    @Enumerated(EnumType.STRING)
//    private BloodGroup bloodGroup;
//    private String phoneNumber;
//    @Lob // Large object for storing large string
//    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
//    private boolean isActive;
}
