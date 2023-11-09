package com.shadril.securityservice.services;

import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.dtos.UserRegistrationRequestDto;
import com.shadril.securityservice.exceptions.CustomException;
import org.apache.catalina.User;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserRegistrationRequestDto userDto) throws CustomException;
    UserDto getUserById(Long id) throws CustomException;
    UserDto getUserByEmail(String email) throws CustomException;
    UserDto updateUser(UserRegistrationRequestDto userDto);
    UserDto deleteUserById(Long id);
}
