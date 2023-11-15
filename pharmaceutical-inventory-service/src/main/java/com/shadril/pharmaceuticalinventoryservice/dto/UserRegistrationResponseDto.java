package com.shadril.pharmaceuticalinventoryservice.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegistrationResponseDto {
    private String message;
    private HttpStatus status;
    private UserDto registeredUserData;
}
