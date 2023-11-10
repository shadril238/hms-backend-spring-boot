package com.shadril.patientservice.controllers;

import com.shadril.patientservice.dtos.PatientDto;
import com.shadril.patientservice.dtos.PatientRegistrationRequestDto;
import com.shadril.patientservice.dtos.PatientRegistrationResponseDto;
import com.shadril.patientservice.exceptions.CustomException;
import com.shadril.patientservice.services.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PatientController {
    @Autowired
    private PatientService patientService;
    @PostMapping("/patient/register")
    public ResponseEntity<PatientRegistrationResponseDto> registerPatient(@RequestBody PatientRegistrationRequestDto registrationDto)
            throws CustomException{
        log.info("Inside registerPatient method of PatientController");
        PatientDto createdPatient = patientService.registerPatient(registrationDto);
        PatientRegistrationResponseDto responseDto =
                new PatientRegistrationResponseDto("Patient registered successfully", HttpStatus.CREATED, createdPatient);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
