package com.shadril.patientservice.service;

import com.shadril.patientservice.dtos.PatientDto;
import com.shadril.patientservice.dtos.PatientRegistrationRequestDto;
import com.shadril.patientservice.exception.CustomException;

public interface PatientService {
    PatientDto registerPatient(PatientRegistrationRequestDto registrationDto) throws CustomException;
}
