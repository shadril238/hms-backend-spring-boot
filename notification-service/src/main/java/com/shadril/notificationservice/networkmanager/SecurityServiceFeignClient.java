package com.shadril.notificationservice.networkmanager;

import com.shadril.notificationservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "security-service", configuration = FeignClientConfiguration.class)
public interface SecurityServiceFeignClient {
    @PostMapping("/users/register")
    ResponseEntity<UserRegistrationResponseDto> register(@RequestBody UserRegistrationRequestDto userDto);

    @PostMapping("/users/login")
    ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto);

    @DeleteMapping("/users/delete/{id}")
    ResponseEntity<ResponseMessageDto> deleteUser(@PathVariable Long id);

    @GetMapping("/users/id/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id);
}
