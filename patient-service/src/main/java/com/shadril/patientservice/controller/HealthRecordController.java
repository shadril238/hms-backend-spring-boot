package com.shadril.patientservice.controller;

import com.shadril.patientservice.dto.HealthRecordDto;
import com.shadril.patientservice.dto.ResponseMessageDto;
import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.service.HealthRecordService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-records")
@Slf4j
public class HealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessageDto> createHealthRecord(@Valid @RequestBody HealthRecordDto healthRecordDto)
            throws CustomException{
        log.info("Inside createHealthRecord method of HealthRecordController");
        healthRecordService.createHealthRecord(healthRecordDto);

        return new ResponseEntity<>(new ResponseMessageDto("Health record created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/id/{healthRecordId}")
    public ResponseEntity<HealthRecordDto> getHealthRecord(@PathVariable Long healthRecordId)
            throws CustomException{
        log.info("Inside getHealthRecord method of HealthRecordController");
        HealthRecordDto healthRecordDto = healthRecordService.getHealthRecord(healthRecordId);
        return new ResponseEntity<>(healthRecordDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<HealthRecordDto>> getAllHealthRecords()
            throws CustomException{
        log.info("Inside getAllHealthRecords method of HealthRecordController");
        List<HealthRecordDto> healthRecordDtos = healthRecordService.getAllHealthRecords();
        return new ResponseEntity<>(healthRecordDtos, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<HealthRecordDto>> getPatientsHealthRecords(@PathVariable String patientId)
            throws CustomException{
        log.info("Inside getPatientsHealthRecords method of HealthRecordController");
        List<HealthRecordDto> healthRecordDtos = healthRecordService.getPatientsHealthRecords(patientId);
        return new ResponseEntity<>(healthRecordDtos, HttpStatus.OK);
    }
}
