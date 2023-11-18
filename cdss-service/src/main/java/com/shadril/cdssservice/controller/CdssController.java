package com.shadril.cdssservice.controller;

import com.shadril.cdssservice.dto.HealthRecommendationDto;
import com.shadril.cdssservice.dto.HealthRecordDto;
import com.shadril.cdssservice.dto.ResponseMessageDto;
import com.shadril.cdssservice.exception.CustomException;
import com.shadril.cdssservice.service.CdssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cdss")
public class CdssController {
    @Autowired
    private CdssService cdssService;

    @GetMapping("/health-recommendation/generate")
    public ResponseEntity<ResponseMessageDto> generateHealthRecommendation()
            throws CustomException {
        log.info("inside generateHealthRecommendation method from CdssController class");
        String response = cdssService.generateHealthRecommendation();
        return new ResponseEntity<>(new ResponseMessageDto(response, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/health-recommendation/all")
    public ResponseEntity<List<HealthRecommendationDto>> getAllHealthRecommendations()
            throws CustomException {
        log.info("inside getAllHealthRecommendations method from CdssController class");
        List<HealthRecommendationDto> response = cdssService.getAllHealthRecommendations();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health-recommendation/{id}")
    public ResponseEntity<HealthRecommendationDto> getHealthRecommendationById(@PathVariable Long id)
            throws CustomException {
        log.info("inside getHealthRecommendationById method from CdssController class");
        HealthRecommendationDto response = cdssService.getHealthRecommendationById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health-recommendation/patient/{patientId}")
    public ResponseEntity<List<HealthRecommendationDto>> getHealthRecommendationByPatientId(@PathVariable String patientId)
            throws CustomException {
        log.info("inside getHealthRecommendationByPatientId method from CdssController class");
        List<HealthRecommendationDto> response = cdssService.getHealthRecommendationByPatientId(patientId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
