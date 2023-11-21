package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.DoctorDto;
import com.shadril.doctorservice.dto.DoctorRegistrationRequestDto;
import com.shadril.doctorservice.dto.DoctorRegistrationResponseDto;
import com.shadril.doctorservice.dto.ResponseMessageDto;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<DoctorRegistrationResponseDto> registerDoctor(@Valid @RequestBody DoctorRegistrationRequestDto doctorRegistrationRequestDto)
            throws CustomException {
        log.info("Received request to register doctor with email: {}", doctorRegistrationRequestDto.getEmail());
        DoctorDto createdDoctor = doctorService.registerDoctor(doctorRegistrationRequestDto);
        log.info("Doctor registered successfully with email: {}", doctorRegistrationRequestDto.getEmail());

        DoctorRegistrationResponseDto doctorRegistrationResponseDto =
                new DoctorRegistrationResponseDto("Doctor registered successfully", HttpStatus.CREATED);

        return new ResponseEntity<>(doctorRegistrationResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/id/{doctorId}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable String doctorId)
            throws CustomException {
        log.info("Received request to get doctor with id: {}", doctorId);
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        log.info("Doctor found with id: {}", doctorId);

        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorDto> getDoctorByEmail(@PathVariable String email)
            throws CustomException {
        log.info("Received request to get doctor with email: {}", email);
        DoctorDto doctorDto = doctorService.getDoctorByEmail(email);
        log.info("Doctor found with email: {}", email);

        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorDto> getCurrentDoctor()
            throws CustomException {
        log.info("Received request to get current doctor");
        DoctorDto doctorDto = doctorService.getCurrentDoctor();
        log.info("Current doctor found");

        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<ResponseMessageDto> updateDoctorProfile(@Valid @RequestBody DoctorDto doctorDto)
            throws CustomException {
        log.info("Received request to update doctor profile");
        doctorService.updateDoctorProfile(doctorDto);
        log.info("Doctor profile updated successfully");

        return new ResponseEntity<>(new ResponseMessageDto("Doctor profile updated successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/approve/{doctorId}/{roomNo}")
    public ResponseEntity<ResponseMessageDto> approveDoctor(@PathVariable String doctorId, @PathVariable String roomNo)
            throws CustomException {
        log.info("Received request to approve doctor with id: {}", doctorId);
        doctorService.approveDoctor(doctorId, roomNo);
        log.info("Doctor approved successfully with id: {}", doctorId);

        return new ResponseEntity<>(new ResponseMessageDto("Doctor approved successfully", HttpStatus.OK), HttpStatus.OK);
    }

}
