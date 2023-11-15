package com.shadril.cdssservice.networkmanager;

import com.shadril.cdssservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "security-service", configuration = FeignClientConfiguration.class)
public interface SecurityServiceFeignClient {
    @PostMapping("/users/register")
    ResponseEntity<UserRegistrationResponseDto> register(@RequestBody UserRegistrationRequestDto userDto);

    @PostMapping("/users/login")
    ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ResponseMessageDto> deleteUser(@PathVariable Long id);
}
