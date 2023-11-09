package com.shadril.patientservice.services.implementations;

import com.shadril.patientservice.dtos.*;
import com.shadril.patientservice.entities.PatientEntity;
import com.shadril.patientservice.enums.Role;
import com.shadril.patientservice.exceptions.CustomException;
import com.shadril.patientservice.networkmanager.SecurityServiceFeignClient;
import com.shadril.patientservice.repository.PatientRepository;
import com.shadril.patientservice.services.PatientService;
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
public class PatientServiceImplementation implements PatientService {

    private final PatientRepository patientRepository;
    private final SecurityServiceFeignClient securityServiceFeignClient;
    private final ModelMapper modelMapper;

    @Override
    public PatientDto createPatient(PatientRegistrationRequestDto registrationDto) throws CustomException {
        UserRegistrationRequestDto userRegistrationRequestDto = new UserRegistrationRequestDto();
        userRegistrationRequestDto.setEmail(registrationDto.getEmail());
        userRegistrationRequestDto.setPassword(registrationDto.getPassword());
        userRegistrationRequestDto.setRole(Role.valueOf("Patient"));
        userRegistrationRequestDto.setActive(true);
        log.info("Sending request to security service");

        ResponseEntity<UserRegistrationResponseDto> responseFromSecurityService = securityServiceFeignClient.register(userRegistrationRequestDto);
        log.info("Response from security service: {}", responseFromSecurityService);

        if (!responseFromSecurityService.getStatusCode().equals(HttpStatus.CREATED)
                || responseFromSecurityService.getBody() == null
                || responseFromSecurityService.getBody().getRegisteredUserData() == null) {
            throw new CustomException(new ResponseMessageDto("User registration failed!", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        PatientEntity patientEntity = modelMapper.map(registrationDto, PatientEntity.class);
        patientEntity.setUserId(responseFromSecurityService.getBody().getRegisteredUserData().getId());
        patientEntity.setActive(true);
        patientEntity.setApproved(false);

        log.info("Saving patient to database");
        PatientEntity savedEntity = patientRepository.save(patientEntity);
        log.info("Patient saved successfully with ID: {}", savedEntity.getPatientId());

        return modelMapper.map(savedEntity, PatientDto.class);
    }
}
