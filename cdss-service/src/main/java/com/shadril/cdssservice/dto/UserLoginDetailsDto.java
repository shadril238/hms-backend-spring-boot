package com.shadril.cdssservice.dto;

import com.shadril.cdssservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDetailsDto {
    private Long userId;
    private String email;
    private Role role;
    private String token;
}
