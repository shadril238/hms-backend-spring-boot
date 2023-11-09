package com.shadril.securityservice.services;

import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.exceptions.UserAlreadyExistException;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> createUser(UserDto userDto) throws UserAlreadyExistException;
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> updateUser(UserDto userDto);
    Optional<UserDto> deleteUserById(Long id);

}
