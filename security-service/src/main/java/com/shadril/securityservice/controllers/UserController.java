package com.shadril.securityservice.controllers;

import com.shadril.securityservice.constants.AppConstants;
import com.shadril.securityservice.dtos.*;
import com.shadril.securityservice.exceptions.CustomException;
import com.shadril.securityservice.services.UserService;
import com.shadril.securityservice.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@RequestBody UserRegistrationRequestDto userDto) throws CustomException{
        log.info("Inside register method of UserController");
        UserDto responseUser = userService.createUser(userDto);
        UserRegistrationResponseDto userRegistrationResponse = new UserRegistrationResponseDto(
                "User registered successfully", HttpStatus.CREATED, responseUser);

        return new ResponseEntity<>(userRegistrationResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) throws CustomException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()));
        UserDto userDto = userService.getUserByEmail(userLoginRequestDto.getEmail());

        List<String> userRoles = new ArrayList<>();
        userRoles.add(String.valueOf(userDto.getRole()));

        String accessToken = JwtUtils.generateToken(userDto.getEmail(), userRoles);

        UserLoginDetailsDto responseDto = new UserLoginDetailsDto(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getRole(),
                AppConstants.TOKEN_PREFIX + accessToken
        );

        UserLoginResponseDto loginResponseDto = UserLoginResponseDto.builder()
                .message("Login successful")
                .status(HttpStatus.OK)
                .userLoginDetails(responseDto)
                .build();

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
