package com.shadril.securityservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponseDto {
    private String message;
    private HttpStatus status;
    private UserLoginDetailsDto userLoginDetails;
}
