package com.shadril.doctorservice.networkmanager;

import com.shadril.doctorservice.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", configuration = FeignClientConfiguration.class)
public interface PatientServiceFeignClient {
    @GetMapping("/patients/email/{email}")
    ResponseEntity<PatientDto> getPatientByEmail(@PathVariable String email);

    @GetMapping("/patients/profile")
    ResponseEntity<PatientDto> getCurrentPatient();

    @GetMapping("/patients/id/{patientId}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable String patientId);
}