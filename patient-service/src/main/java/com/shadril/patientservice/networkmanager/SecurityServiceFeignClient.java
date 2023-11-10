package com.shadril.patientservice.networkmanager;

import com.shadril.patientservice.dto.UserLoginRequestDto;
import com.shadril.patientservice.dto.UserLoginResponseDto;
import com.shadril.patientservice.dto.UserRegistrationRequestDto;
import com.shadril.patientservice.dto.UserRegistrationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "security-service", configuration = FeignClientConfiguration.class)
public interface SecurityServiceFeignClient {
    @PostMapping("/users/register")
    ResponseEntity<UserRegistrationResponseDto> register(@RequestBody UserRegistrationRequestDto userDto);

    @PostMapping("/users/login")
    ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto);
}
