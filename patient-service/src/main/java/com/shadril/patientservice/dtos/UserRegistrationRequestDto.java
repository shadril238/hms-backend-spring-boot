package com.shadril.patientservice.dtos;

import com.shadril.patientservice.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequestDto {
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isActive;
}
