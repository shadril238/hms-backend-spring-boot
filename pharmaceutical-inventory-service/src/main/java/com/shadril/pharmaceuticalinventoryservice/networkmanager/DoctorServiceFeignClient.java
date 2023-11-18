package com.shadril.pharmaceuticalinventoryservice.networkmanager;

import com.shadril.pharmaceuticalinventoryservice.dto.DoctorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "doctor-service", configuration = FeignClientConfiguration.class)
public interface DoctorServiceFeignClient {
    @GetMapping("/doctors/profile")
    ResponseEntity<DoctorDto> getCurrentDoctor();
}
