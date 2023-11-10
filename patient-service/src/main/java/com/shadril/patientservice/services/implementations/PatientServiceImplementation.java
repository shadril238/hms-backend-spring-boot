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
    public PatientDto registerPatient(PatientRegistrationRequestDto registrationDto) throws CustomException {
        try {
            UserRegistrationRequestDto userRegistrationRequestDto = new UserRegistrationRequestDto();
            userRegistrationRequestDto.setEmail(registrationDto.getEmail());
            userRegistrationRequestDto.setPassword(registrationDto.getPassword());
            userRegistrationRequestDto.setRole(Role.valueOf("Patient"));
            userRegistrationRequestDto.setActive(true);

            log.info("Sending request to security service for patient registration");
            ResponseEntity<UserRegistrationResponseDto> response = securityServiceFeignClient.register(userRegistrationRequestDto);

            if (response.getStatusCode() != HttpStatus.CREATED || response.getBody() == null) {
                throw new CustomException(new ResponseMessageDto("Registration failed at security service", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            PatientEntity patientEntity = modelMapper.map(registrationDto, PatientEntity.class);
            patientEntity.setUserId(response.getBody().getRegisteredUserData().getId());
            patientEntity.setActive(true);
            patientEntity.setApproved(false);

            log.info("Saving patient to database");
            PatientEntity savedEntity = patientRepository.save(patientEntity);
            log.info("Patient saved successfully with ID: {}", savedEntity.getPatientId());

            return modelMapper.map(savedEntity, PatientDto.class);
        } catch (CustomException e) {
            log.error("Error occurred during patient registration: {}", e.getMessage());
            throw e;
        }
    }
}
