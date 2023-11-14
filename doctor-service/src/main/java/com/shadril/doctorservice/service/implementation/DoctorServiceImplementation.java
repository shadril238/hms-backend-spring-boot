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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            log.info("inside registerDoctor method in DoctorServiceImplementation");
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

    @Override
    public DoctorDto getDoctorById(String doctorId)
            throws CustomException {
        try {
            log.info("inside getDoctorById method in DoctorServiceImplementation");
            Optional<DoctorEntity> doctorEntity = doctorRepository.findById(doctorId);
            if (doctorEntity.isPresent()) {
                return modelMapper.map(doctorEntity.get(), DoctorDto.class);
            } else {
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            log.error("Error occurred during getting doctor by id: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public DoctorDto getDoctorByEmail(String email) throws CustomException {
        try{
            log.info("inside getDoctorByEmail method in DoctorServiceImplementation");
            Optional<DoctorEntity> doctorEntity = doctorRepository.findByEmail(email);
            if (doctorEntity.isPresent() && doctorEntity.get().isActive()) {
                return modelMapper.map(doctorEntity.get(), DoctorDto.class);
            } else {
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            log.error("Error occurred during getting doctor by email: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public DoctorDto getCurrentDoctor() throws CustomException {
        try{
            log.info("inside getCurrentDoctor method in DoctorServiceImplementation");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to access this resource", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            String email = authentication.getName();
            log.info("Getting doctor by email: {}", email);

            Optional<DoctorEntity> doctorEntity = doctorRepository.findByEmail(email);
            return doctorEntity
                    .map(entity -> modelMapper.map(entity, DoctorDto.class))
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Token is invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST));
        } catch (Exception ex) {
            log.error("Error occurred while getting current patient: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Token is invalid", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateDoctorProfile(DoctorDto doctorDto) throws CustomException {
        try{
            log.info("inside updateDoctorProfile method in DoctorServiceImplementation");
            Optional<DoctorEntity> doctorEntityOptional = doctorRepository.findById(doctorDto.getDoctorId());
            if (doctorEntityOptional.isEmpty() || !doctorEntityOptional.get().isActive()) {
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Doctor found with id: {}", doctorDto.getDoctorId());

            DoctorEntity doctorEntity = doctorEntityOptional.get();
            doctorEntity.setFirstName(doctorDto.getFirstName() != null ? doctorDto.getFirstName() : doctorEntity.getFirstName());
            doctorEntity.setLastName(doctorDto.getLastName() != null ? doctorDto.getLastName() : doctorEntity.getLastName());
            doctorEntity.setCareerTitle(doctorDto.getCareerTitle() != null ? doctorDto.getCareerTitle() : doctorEntity.getCareerTitle());
            doctorEntity.setDepartment(doctorDto.getDepartment() != null ? doctorDto.getDepartment() : doctorEntity.getDepartment());
            doctorEntity.setSpecialization(doctorDto.getSpecialization() != null ? doctorDto.getSpecialization() : doctorEntity.getSpecialization());
            doctorEntity.setDesignation(doctorDto.getDesignation() != null ? doctorDto.getDesignation() : doctorEntity.getDesignation());
            doctorEntity.setInstitute(doctorDto.getInstitute() != null ? doctorDto.getInstitute() : doctorEntity.getInstitute());
            doctorEntity.setExperienceYears(doctorDto.getExperienceYears() != null ? doctorDto.getExperienceYears() : doctorEntity.getExperienceYears());
            doctorEntity.setQualifications(doctorDto.getQualifications() != null ? doctorDto.getQualifications() : doctorEntity.getQualifications());
            doctorEntity.setLicenseNumber(doctorDto.getLicenseNumber() != null ? doctorDto.getLicenseNumber() : doctorEntity.getLicenseNumber());
            doctorEntity.setDateOfBirth(doctorDto.getDateOfBirth() != null ? doctorDto.getDateOfBirth() : doctorEntity.getDateOfBirth());
            doctorEntity.setGender(doctorDto.getGender() != null ? doctorDto.getGender() : doctorEntity.getGender());
            doctorEntity.setBloodGroup(doctorDto.getBloodGroup() != null ? doctorDto.getBloodGroup() : doctorEntity.getBloodGroup());
            doctorEntity.setPhoneNumber(doctorDto.getPhoneNumber() != null ? doctorDto.getPhoneNumber() : doctorEntity.getPhoneNumber());
            doctorEntity.setBiography(doctorDto.getBiography() != null ? doctorDto.getBiography() : doctorEntity.getBiography());
            doctorEntity.setAddress(doctorDto.getAddress() != null ? doctorDto.getAddress() : doctorEntity.getAddress());

            log.info("Saving updated doctor data to database");
            doctorRepository.save(doctorEntity);
            log.info("Doctor updated successfully with id: {}", doctorDto.getDoctorId());
        } catch (CustomException ex) {
            log.error("Error occurred during updating doctor profile: {}", ex.getMessage());
            throw ex;
        }
    }
}
