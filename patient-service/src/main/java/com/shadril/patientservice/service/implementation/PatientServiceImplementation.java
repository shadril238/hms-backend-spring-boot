package com.shadril.patientservice.service.implementation;

import com.shadril.patientservice.constants.AppConstants;
import com.shadril.patientservice.dto.*;
import com.shadril.patientservice.entity.PatientEntity;
import com.shadril.patientservice.enums.Role;
import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.networkmanager.SecurityServiceFeignClient;
import com.shadril.patientservice.repository.PatientRepository;
import com.shadril.patientservice.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public PatientDto getPatientById(String patientId)
            throws CustomException {
        Optional<PatientEntity> patientEntity = patientRepository.findById(patientId);
        if (patientEntity.isPresent()) {
            return modelMapper.map(patientEntity.get(), PatientDto.class);
        } else {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PatientDto getPatientByEmail(String email)
            throws CustomException {
        Optional<PatientEntity> patientEntity = patientRepository.findByEmail(email);
        if (patientEntity.isPresent()) {
            return modelMapper.map(patientEntity.get(), PatientDto.class);
        } else {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PatientDto getCurrentPatient() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Getting patient by email: {}", email);

        Optional<PatientEntity> patientEntity = patientRepository.findByEmail(email);

        return patientEntity.map(entity -> modelMapper.map(entity, PatientDto.class))
                .orElseThrow(() -> new CustomException(new ResponseMessageDto("Token is invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
    }

    @Override
    public void updatePatientProfile(PatientDto patientDto)
            throws CustomException {
        Optional<PatientEntity> patientEntityOptional = patientRepository.findById(patientDto.getPatientId());
        if (patientEntityOptional.isPresent()) {
            PatientEntity patientEntity = patientEntityOptional.get();
            patientEntity.setFirstName(patientDto.getFirstName() != null ? patientDto.getFirstName() : patientEntity.getFirstName());
            patientEntity.setLastName(patientDto.getLastName() != null ? patientDto.getLastName() : patientEntity.getLastName());
            patientEntity.setDateOfBirth(patientDto.getDateOfBirth() != null ? patientDto.getDateOfBirth() : patientEntity.getDateOfBirth());
            patientEntity.setGender(patientDto.getGender() != null ? patientDto.getGender() : patientEntity.getGender());
            patientEntity.setBloodGroup(patientDto.getBloodGroup() != null ? patientDto.getBloodGroup() : patientEntity.getBloodGroup());
            patientEntity.setPhoneNumber(patientDto.getPhoneNumber() != null ? patientDto.getPhoneNumber() : patientEntity.getPhoneNumber());
            patientEntity.setAddress(patientDto.getAddress() != null ? patientDto.getAddress() : patientEntity.getAddress());
        } else {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

}
