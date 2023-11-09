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
public class UserDto {
    private Long id;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
