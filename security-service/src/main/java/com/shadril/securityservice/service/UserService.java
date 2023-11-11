package com.shadril.securityservice.service;

import com.shadril.securityservice.dto.UserDto;
import com.shadril.securityservice.dto.UserRegistrationRequestDto;
import com.shadril.securityservice.exception.CustomException;

public interface UserService {
    UserDto createUser(UserRegistrationRequestDto userDto) throws CustomException;
    UserDto getUserById(Long id) throws CustomException;
    UserDto getUserByEmail(String email) throws CustomException;
    UserDto updateUser(UserRegistrationRequestDto userDto);
    void deleteUserById(Long id) throws CustomException;
}
