package com.shadril.patientservice.dtos;

import com.shadril.patientservice.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegistrationRequestDto {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private boolean isActive;
}
