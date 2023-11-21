package com.shadril.patientservice.controller;

import com.shadril.patientservice.dto.PatientDto;
import com.shadril.patientservice.dto.PatientRegistrationRequestDto;
import com.shadril.patientservice.dto.PatientRegistrationResponseDto;
import com.shadril.patientservice.dto.ResponseMessageDto;
import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Slf4j
public class PatientController {
    @Autowired
    private PatientService patientService;
    @PostMapping("/register")
    public ResponseEntity<PatientRegistrationResponseDto> registerPatient(@Valid @RequestBody PatientRegistrationRequestDto registrationDto)
            throws CustomException{
        log.info("Inside registerPatient method of PatientController");
        PatientDto createdPatient = patientService.registerPatient(registrationDto);
        PatientRegistrationResponseDto responseDto =
                new PatientRegistrationResponseDto("Patient registered successfully", HttpStatus.CREATED);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/id/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable String patientId)
            throws CustomException{
        log.info("Inside getPatientById method of PatientController");
        PatientDto patientDto = patientService.getPatientById(patientId);
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PatientDto> getPatientByEmail(@PathVariable String email)
            throws CustomException{
        log.info("Inside getPatientByEmail method of PatientController");
        PatientDto patientDto = patientService.getPatientByEmail(email);
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientDto> getCurrentPatient()
            throws CustomException{
        log.info("Inside getCurrentPatient method of PatientController");
        PatientDto patientDto = patientService.getCurrentPatient();
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<ResponseMessageDto> updatePatientProfile(@Valid @RequestBody PatientDto patientDto)
            throws CustomException{
        log.info("Inside updatePatientProfile method of PatientController");
        patientService.updatePatientProfile(patientDto);
        ResponseMessageDto responseMessageDto = new ResponseMessageDto("Patient profile updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDto>> getAllPatients()
            throws CustomException{
        log.info("Inside getAllPatients method of PatientController");
        List<PatientDto> patientDto = patientService.getAllPatients();
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

}
