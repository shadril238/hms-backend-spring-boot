package com.shadril.doctorservice.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRegistrationResponseDto {
    private String message;
    private HttpStatus status;
//    private DoctorDto registeredDoctorData;
}
