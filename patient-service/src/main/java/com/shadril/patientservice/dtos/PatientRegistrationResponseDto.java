package com.shadril.patientservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRegistrationResponseDto {
    private String message;
    private HttpStatus status;
    private PatientDto registeredPatientData;
}
