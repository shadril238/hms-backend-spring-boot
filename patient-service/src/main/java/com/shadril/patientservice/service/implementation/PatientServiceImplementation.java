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

import java.util.List;
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
        if(patientEntity.isEmpty() || !patientEntity.get().isActive()){
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("Patient found with ID: {}", patientId);
        return modelMapper.map(patientEntity.get(), PatientDto.class);
    }

    @Override
    public PatientDto getPatientByEmail(String email)
            throws CustomException {
        Optional<PatientEntity> patientEntity = patientRepository.findByEmail(email);
        if(patientEntity.isEmpty() || !patientEntity.get().isActive()){
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("Patient found with email: {}", email);
        return modelMapper.map(patientEntity.get(), PatientDto.class);
    }

    @Override
    public PatientDto getCurrentPatient() throws CustomException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to access this resource", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            String email = authentication.getName();
            log.info("Getting patient by email: {}", email);

            Optional<PatientEntity> patientEntity = patientRepository.findByEmail(email);

            return patientEntity.map(entity -> modelMapper.map(entity, PatientDto.class))
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Token is invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
        } catch (Exception ex) {
            log.error("Error occurred while getting current patient: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Token is invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updatePatientProfile(PatientDto patientDto)
            throws CustomException {
        Optional<PatientEntity> patientEntityOptional = patientRepository.findById(patientDto.getPatientId());
        if (patientEntityOptional.isEmpty() || !patientEntityOptional.get().isActive()) {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        log.info("Patient found with ID: {}", patientDto.getPatientId());

        PatientEntity patientEntity = patientEntityOptional.get();
        patientEntity.setFirstName(patientDto.getFirstName() != null ? patientDto.getFirstName() : patientEntity.getFirstName());
        patientEntity.setLastName(patientDto.getLastName() != null ? patientDto.getLastName() : patientEntity.getLastName());
        patientEntity.setDateOfBirth(patientDto.getDateOfBirth() != null ? patientDto.getDateOfBirth() : patientEntity.getDateOfBirth());
        patientEntity.setGender(patientDto.getGender() != null ? patientDto.getGender() : patientEntity.getGender());
        patientEntity.setBloodGroup(patientDto.getBloodGroup() != null ? patientDto.getBloodGroup() : patientEntity.getBloodGroup());
        patientEntity.setPhoneNumber(patientDto.getPhoneNumber() != null ? patientDto.getPhoneNumber() : patientEntity.getPhoneNumber());
        patientEntity.setAddress(patientDto.getAddress() != null ? patientDto.getAddress() : patientEntity.getAddress());

        patientRepository.save(patientEntity);
    }

    @Override
    public List<PatientDto> getAllPatients() throws CustomException {
        try{
            List<PatientEntity> patientEntities = patientRepository.findAll();
            return patientEntities.stream().map(entity -> modelMapper.map(entity, PatientDto.class)).toList();
        } catch (Exception ex) {
            log.error("Error occurred while getting all patients: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while getting all patients", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<PatientDto> getAllApprovedPatients() throws CustomException {
        try{
            List<PatientEntity> patientEntities = patientRepository.findAllByApproved(true);
            return patientEntities.stream().map(entity -> modelMapper.map(entity, PatientDto.class)).toList();
        } catch (Exception ex) {
            log.error("Error occurred while getting all approved patients: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while getting all approved patients", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<PatientDto> getAllUnapprovedPatients() throws CustomException {
        try{
            List<PatientEntity> patientEntities = patientRepository.findAllByApproved(false);
            return patientEntities.stream().map(entity -> modelMapper.map(entity, PatientDto.class)).toList();
        } catch (Exception ex) {
            log.error("Error occurred while getting all unapproved patients: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while getting all unapproved patients", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void approvePatient(String patientId) throws CustomException {
        try{
            Optional<PatientEntity> patientEntityOptional = patientRepository.findById(patientId);
            if (patientEntityOptional.isEmpty() || !patientEntityOptional.get().isActive()) {
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Patient found with ID: {}", patientId);

            if (patientEntityOptional.get().isApproved()) {
                throw new CustomException(new ResponseMessageDto("Patient is already approved", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            PatientEntity patientEntity = patientEntityOptional.get();
            patientEntity.setApproved(true);

            patientRepository.save(patientEntity);
        } catch (Exception ex) {
            log.error("Error occurred while approving patient: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while approving patient", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PatientDto getPatientByUserId(Long userId) throws CustomException {
        try{
            Optional<PatientEntity> patientEntityOptional = patientRepository.findByUserId(userId);
            if (patientEntityOptional.isEmpty() || !patientEntityOptional.get().isActive()) {
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Patient found with ID: {}", patientEntityOptional.get().getPatientId());

            return modelMapper.map(patientEntityOptional.get(), PatientDto.class);
        } catch (Exception ex) {
            log.error("Error occurred while getting patient by user ID: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while getting patient by user ID", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Long countTotalPatients() throws CustomException {
        try{
            return patientRepository.count();
        } catch (Exception ex) {
            log.error("Error occurred while counting total patients: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while counting total patients", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<PatientDto> searchPatientByName(String name) throws CustomException {
        try{
            List<PatientEntity> patientEntities = patientRepository.searchByFirstNameOrLastNameContainingIgnoreCase(name);
            return patientEntities.stream().map(entity -> modelMapper.map(entity, PatientDto.class)).toList();
        } catch (Exception ex) {
            log.error("Error occurred while searching patient by name: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Error occurred while searching patient by name", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

}
