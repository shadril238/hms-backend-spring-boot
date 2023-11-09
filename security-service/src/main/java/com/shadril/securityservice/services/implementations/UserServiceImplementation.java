package com.shadril.securityservice.services.implementations;

import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.entities.UserEntity;
import com.shadril.securityservice.exceptions.UserAlreadyExistException;
import com.shadril.securityservice.repositories.UserRepository;
import com.shadril.securityservice.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public Optional<UserDto> createUser(UserDto userDto) throws UserAlreadyExistException{
        // Check if user already exists by email
        userRepository.findUserByEmail(userDto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistException(userDto.getEmail());
        });

        // Convert UserDto to UserEntity
        UserEntity userEntity = convertToEntity(userDto);

        // Save the UserEntity to the database
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Convert the saved UserEntity back to UserDto
        UserDto savedUserDto = convertToDto(savedUserEntity);

        // Return the saved UserDto wrapped in an Optional
        return Optional.of(savedUserDto);
    }


    @Override
    public Optional<UserDto> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> updateUser(UserDto userDto) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> deleteUserById(Long id) {
        return Optional.empty();
    }

    // Convert UserDto to UserEntity
    private UserEntity convertToEntity(UserDto userDto) {
        return UserEntity.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .dateOfBirth(userDto.getDateOfBirth())
                .gender(userDto.getGender())
                .bloodGroup(userDto.getBloodGroup())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .role(userDto.getRole())
                .isActive(userDto.isActive())
                .build();
    }

    // Convert UserEntity to UserDto
    private UserDto convertToDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getDateOfBirth(),
                userEntity.getGender(),
                userEntity.getBloodGroup(),
                userEntity.getPhoneNumber(),
                userEntity.getAddress(),
                userEntity.getRole(),
                userEntity.isActive()
        );
    }

}
