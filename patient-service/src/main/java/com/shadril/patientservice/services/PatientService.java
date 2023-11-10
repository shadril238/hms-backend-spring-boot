package com.shadril.patientservice.services;

import com.shadril.patientservice.dtos.PatientDto;
import com.shadril.patientservice.dtos.PatientRegistrationRequestDto;
import com.shadril.patientservice.exceptions.CustomException;

public interface PatientService {
    PatientDto registerPatient(PatientRegistrationRequestDto registrationDto) throws CustomException;
}
