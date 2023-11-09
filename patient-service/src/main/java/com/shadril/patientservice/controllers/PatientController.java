package com.shadril.patientservice.controllers;

import com.shadril.patientservice.dtos.PatientDto;
import com.shadril.patientservice.dtos.PatientRegistrationRequestDto;
import com.shadril.patientservice.exceptions.CustomException;
import com.shadril.patientservice.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;
    @PostMapping("/patient/register")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRegistrationRequestDto registrationDto) throws CustomException{
        PatientDto createdPatient = patientService.createPatient(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }
}
