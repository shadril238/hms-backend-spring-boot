package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.DoctorDto;
import com.shadril.doctorservice.dto.DoctorRegistrationRequestDto;
import com.shadril.doctorservice.dto.DoctorRegistrationResponseDto;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/doctor/register")
    public ResponseEntity<DoctorRegistrationResponseDto> registerDoctor(@Valid @RequestBody DoctorRegistrationRequestDto doctorRegistrationRequestDto)
            throws CustomException {
        log.info("Received request to register doctor with email: {}", doctorRegistrationRequestDto.getEmail());
        DoctorDto createdDoctor = doctorService.registerDoctor(doctorRegistrationRequestDto);
        log.info("Doctor registered successfully with email: {}", doctorRegistrationRequestDto.getEmail());

        DoctorRegistrationResponseDto doctorRegistrationResponseDto =
                new DoctorRegistrationResponseDto("Doctor registered successfully", HttpStatus.CREATED, createdDoctor);

        return new ResponseEntity<>(doctorRegistrationResponseDto, HttpStatus.CREATED);
    }
}
