package com.shadril.securityservice.controllers;

import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.dtos.UserRegistrationRequestDto;
import com.shadril.securityservice.dtos.UserRegistrationResponseDto;
import com.shadril.securityservice.exceptions.CustomException;
import com.shadril.securityservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequestDto userDto) throws CustomException{
        log.info("Inside register method of UserController");
        UserDto responseUser = userService.createUser(userDto);
        UserRegistrationResponseDto userRegistrationResponse = new UserRegistrationResponseDto(
                "User registered successfully", HttpStatus.CREATED, responseUser);

        return new ResponseEntity<>(userRegistrationResponse, HttpStatus.CREATED);
    }
}
