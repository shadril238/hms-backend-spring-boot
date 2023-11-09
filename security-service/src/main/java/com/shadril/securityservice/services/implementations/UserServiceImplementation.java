package com.shadril.securityservice.services.implementations;

import com.shadril.securityservice.dtos.ResponseMessageDto;
import com.shadril.securityservice.dtos.UserDto;
import com.shadril.securityservice.dtos.UserRegistrationRequestDto;
import com.shadril.securityservice.entities.UserEntity;
import com.shadril.securityservice.exceptions.CustomException;
import com.shadril.securityservice.repositories.UserRepository;
import com.shadril.securityservice.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDto createUser(UserRegistrationRequestDto userDto)
            throws CustomException {

        if(userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
            String errorMessage = "User with email " + userDto.getEmail() + " already exists";
            throw new CustomException(new ResponseMessageDto(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setRole(userDto.getRole());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userEntity.setBloodGroup(userDto.getBloodGroup());
        userEntity.setDateOfBirth(userDto.getDateOfBirth());
        userEntity.setGender(userDto.getGender());
        userEntity.setActive(true);


        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User created with email: {}", userDto.getEmail());

        UserDto returnedUser = modelMapper.map(savedUser, UserDto.class);
        List<String> userRoles = new ArrayList<>();
        userRoles.add(String.valueOf(userDto.getRole()));
        return returnedUser;
    }


    @Override
    public Optional<UserRegistrationRequestDto> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserRegistrationRequestDto> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserRegistrationRequestDto> updateUser(UserRegistrationRequestDto userDto) {
        return Optional.empty();
    }

    @Override
    public Optional<UserRegistrationRequestDto> deleteUserById(Long id) {
        return Optional.empty();
    }

}
