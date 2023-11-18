package com.shadril.cdssservice.networkmanager;

import com.shadril.cdssservice.dto.HealthRecordDto;
import com.shadril.cdssservice.dto.PatientDto;
import com.shadril.cdssservice.dto.ResponseMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "patient-service", configuration = FeignClientConfiguration.class)
public interface PatientServiceFeignClient {
    @GetMapping("/patients/email/{email}")
    ResponseEntity<PatientDto> getPatientByEmail(@PathVariable String email);

    @GetMapping("/patients/profile")
    ResponseEntity<PatientDto> getCurrentPatient();

    @GetMapping("/patients/id/{patientId}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable String patientId);

    @GetMapping("/patients/health-records/id/{healthRecordId}")
    ResponseEntity<HealthRecordDto> getHealthRecord(@PathVariable Long healthRecordId);

    @GetMapping("/patients/health-records/all")
    ResponseEntity<List<HealthRecordDto>> getAllHealthRecords();

    @GetMapping("/patients/health-records/patient/{patientId}")
    ResponseEntity<List<HealthRecordDto>> getPatientsHealthRecords(@PathVariable String patientId);

    @PostMapping("/patients/health-records/create")
    ResponseEntity<ResponseMessageDto> createHealthRecord(@RequestBody HealthRecordDto healthRecordDto);

}