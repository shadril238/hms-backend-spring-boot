package com.shadril.doctorservice.service.implementation;

import com.shadril.doctorservice.dto.*;
import com.shadril.doctorservice.entitiy.DoctorEntity;
import com.shadril.doctorservice.enums.Role;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.networkmanager.SecurityServiceFeignClient;
import com.shadril.doctorservice.repository.DoctorRepository;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImplementation implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SecurityServiceFeignClient securityServiceFeignClient;
    private final ModelMapper modelMapper;

    @Override
    public DoctorDto registerDoctor(DoctorRegistrationRequestDto doctorRegistrationRequestDto)
            throws CustomException {
        try {
            UserRegistrationRequestDto userRegistrationRequestDto = new UserRegistrationRequestDto();
            userRegistrationRequestDto.setEmail(doctorRegistrationRequestDto.getEmail());
            userRegistrationRequestDto.setPassword(doctorRegistrationRequestDto.getPassword());
            userRegistrationRequestDto.setRole(Role.valueOf("Doctor"));
            userRegistrationRequestDto.setActive(true);

            log.info("Sending request to security-service for doctor registration");
            ResponseEntity<UserRegistrationResponseDto> response = securityServiceFeignClient.register(userRegistrationRequestDto);

            if (response.getStatusCode() != HttpStatus.CREATED || response.getBody() == null) {
                throw new CustomException(new ResponseMessageDto("Registration failed at security service", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            log.info("Doctor registered at security-service successfully");

            DoctorEntity doctorEntity = modelMapper.map(doctorRegistrationRequestDto, DoctorEntity.class);
            doctorEntity.setUserId(response.getBody().getRegisteredUserData().getId());
            doctorEntity.setActive(true);
            doctorEntity.setApproved(false);

            log.info("Saving doctor data to database");
            DoctorEntity savedDoctorEntity = doctorRepository.save(doctorEntity);
            log.info("Doctor saved successfully with id: {}", savedDoctorEntity.getDoctorId());

            return modelMapper.map(savedDoctorEntity, DoctorDto.class);
        } catch (CustomException e) {
            log.error("Error occurred during patient registration: {}", e.getMessage());
            throw e;
        }
    }
}
