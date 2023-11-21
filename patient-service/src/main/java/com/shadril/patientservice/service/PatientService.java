package com.shadril.patientservice.service;

import com.shadril.patientservice.dto.PatientDto;
import com.shadril.patientservice.dto.PatientRegistrationRequestDto;
import com.shadril.patientservice.exception.CustomException;

import java.util.List;

public interface PatientService {
    PatientDto registerPatient(PatientRegistrationRequestDto registrationDto) throws CustomException;

    PatientDto getPatientById(String patientId) throws CustomException;

    PatientDto getPatientByEmail(String email) throws CustomException;

    PatientDto getCurrentPatient() throws CustomException;

    void updatePatientProfile(PatientDto patientDto) throws CustomException;

    List<PatientDto> getAllPatients() throws CustomException;
}
