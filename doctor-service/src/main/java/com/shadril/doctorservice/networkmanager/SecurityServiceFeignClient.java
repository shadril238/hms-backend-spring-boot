package com.shadril.doctorservice.networkmanager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "security-service", configuration = FeignClientConfiguration.class)
public interface SecurityServiceFeignClient {
}
