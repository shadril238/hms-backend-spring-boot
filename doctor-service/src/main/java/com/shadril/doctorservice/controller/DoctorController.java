package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.*;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/department/list")
    public ResponseEntity<List<String>> getDoctorDepartmentList()
            throws CustomException {
        log.info("Received request to get doctor department list");
        List<String> departmentList = doctorService.getDoctorDepartmentList();
        log.info("Doctor department list fetched successfully");

        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<DoctorDto>> getDoctorListByDepartment(@PathVariable String department)
            throws CustomException {
        log.info("Received request to get doctor list by department");
        List<DoctorDto> doctorList = doctorService.getDoctorListByDepartment(department);
        log.info("Doctor list fetched successfully");

        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DoctorDto>> getDoctorList()
            throws CustomException {
        log.info("Received request to get doctor list");
        List<DoctorDto> doctorList = doctorService.getDoctorList();
        log.info("Doctor list fetched successfully");

        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @PostMapping("/approve/allocate/{doctorId}/{roomNo}")
    public ResponseEntity<ResponseMessageDto> approveDoctorAndAllocateRoom(@PathVariable String doctorId, @PathVariable String roomNo)
            throws CustomException {
        log.info("Received request to approve doctor and allocate room with id: {}", doctorId);
        doctorService.approveDoctorAndAllocateRoom(doctorId, roomNo);
        log.info("Doctor approved and room allocated successfully with id: {}", doctorId);

        return new ResponseEntity<>(new ResponseMessageDto("Doctor approved and room allocated successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/approved/list")
    public ResponseEntity<List<DoctorDto>> getApprovedDoctorList()
            throws CustomException {
        log.info("Received request to get approved doctor list");
        List<DoctorDto> doctorList = doctorService.getApprovedDoctorList();
        log.info("Approved doctor list fetched successfully");

        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTotalDoctors()
            throws CustomException {
        log.info("Received request to count total doctors");
        Long count = doctorService.countTotalDoctors();
        log.info("Total doctors counted successfully");

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<DoctorDto>> searchDoctorByName(@PathVariable String name)
            throws CustomException {
        log.info("Received request to search doctor by name");
        List<DoctorDto> doctorList = doctorService.searchDoctorByName(name);
        log.info("Doctor list fetched successfully");

        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @GetMapping("/search/department/{department}")
    public ResponseEntity<List<DoctorDto>> searchDoctorByDepartment(@PathVariable String department)
            throws CustomException {
        log.info("Received request to search doctor by department");
        List<DoctorDto> doctorList = doctorService.searchDoctorByDepartment(department);
        log.info("Doctor list fetched successfully");

        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }
}
