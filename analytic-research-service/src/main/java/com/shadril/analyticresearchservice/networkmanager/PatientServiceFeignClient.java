package com.shadril.analyticresearchservice.networkmanager;

import com.shadril.analyticresearchservice.dto.HealthRecordDto;
import com.shadril.analyticresearchservice.dto.ResponseMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "patient-service", configuration = FeignClientConfiguration.class)
public interface PatientServiceFeignClient {
    @GetMapping("/patients/health-records/id/{healthRecordId}")
    ResponseEntity<HealthRecordDto> getHealthRecord(@PathVariable Long healthRecordId);

    @GetMapping("/patients/health-records/all")
    ResponseEntity<List<HealthRecordDto>> getAllHealthRecords();

    @GetMapping("/patients/health-records/patient/{patientId}")
    ResponseEntity<List<HealthRecordDto>> getPatientsHealthRecords(@PathVariable String patientId);

    @PostMapping("/patients/health-records/create")
    ResponseEntity<ResponseMessageDto> createHealthRecord(@RequestBody HealthRecordDto healthRecordDto);

}