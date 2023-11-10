package com.shadril.securityservice.dto;

import com.shadril.securityservice.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
    @NotNull
    private boolean isActive;
}
