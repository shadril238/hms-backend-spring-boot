package com.shadril.securityservice.service.implementation;

import com.shadril.securityservice.dto.ResponseMessageDto;
import com.shadril.securityservice.dto.UserDto;
import com.shadril.securityservice.dto.UserRegistrationRequestDto;
import com.shadril.securityservice.entity.UserEntity;
import com.shadril.securityservice.exception.CustomException;
import com.shadril.securityservice.repository.UserRepository;
import com.shadril.securityservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        return new User(userEntity.getEmail(),userEntity.getPassword(),
                true,true,true,true,new ArrayList<>());
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
        userEntity.setActive(true);

        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User created with email: {}", userDto.getEmail());

        UserDto returnedUser = modelMapper.map(savedUser, UserDto.class);
        List<String> userRoles = new ArrayList<>();
        userRoles.add(String.valueOf(userDto.getRole()));
        return returnedUser;
    }


    @Override
    public UserDto getUserById(Long id)
            throws CustomException{

        String errorMessage = "No user found with id: " + id;
        UserEntity userEntity = userRepository
                .findUserById(id)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto  getUserByEmail(String email)
            throws CustomException {

        String errorMessage = "No user found with email: " + email;
        UserEntity userEntity = userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new CustomException(new ResponseMessageDto(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserRegistrationRequestDto userDto) {
        return null;
    }

    @Override
    public UserDto deleteUserById(Long id) {
        return null;
    }

}
