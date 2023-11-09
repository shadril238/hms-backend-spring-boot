package com.shadril.securityservice.services;

import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.dtos.UserRegistrationRequestDto;
import com.shadril.securityservice.exceptions.CustomException;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserRegistrationRequestDto userDto) throws CustomException;
    Optional<UserRegistrationRequestDto> getUserById(Long id);
    Optional<UserRegistrationRequestDto> getUserByEmail(String email);
    Optional<UserRegistrationRequestDto> updateUser(UserRegistrationRequestDto userDto);
    Optional<UserRegistrationRequestDto> deleteUserById(Long id);
}
