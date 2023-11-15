package com.shadril.pharmaceuticalinventoryservice.dto;


import com.shadril.pharmaceuticalinventoryservice.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private Role role;
}
