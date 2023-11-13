package com.shadril.patientservice.service.implementation;

import com.shadril.patientservice.dto.HealthRecordDto;
import com.shadril.patientservice.dto.PatientDto;
import com.shadril.patientservice.dto.ResponseMessageDto;
import com.shadril.patientservice.entity.HealthRecordEntity;
import com.shadril.patientservice.entity.PatientEntity;
import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.repository.HealthRecordRepository;
import com.shadril.patientservice.repository.PatientRepository;
import com.shadril.patientservice.service.HealthRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HealthRecordServiceImplementation implements HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createHealthRecord(HealthRecordDto healthRecordDto)
            throws CustomException {
        Optional<PatientEntity> patientEntity = patientRepository.findById(healthRecordDto.getPatientId());
        if (patientEntity.isEmpty() || !patientEntity.get().isActive()) {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        HealthRecordEntity healthRecordEntity = modelMapper.map(healthRecordDto, HealthRecordEntity.class);
        healthRecordEntity.setPatient(patientEntity.get());
        healthRecordEntity.setIsActive(true);

        HealthRecordEntity savedEntity = healthRecordRepository.save(healthRecordEntity);
        log.info("Health record saved successfully with ID: {}", savedEntity.getHealthRecordId());
    }

    @Override
    public HealthRecordDto getHealthRecord(Long healthRecordId) throws CustomException {
        Optional<HealthRecordEntity> healthRecordEntity = healthRecordRepository.findById(healthRecordId);
        if (healthRecordEntity.isEmpty() || !healthRecordEntity.get().getIsActive()) {
            throw new CustomException(new ResponseMessageDto("Health record not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        return modelMapper.map(healthRecordEntity.get(), HealthRecordDto.class);
    }

    @Override
    public List<HealthRecordDto> getAllHealthRecords() throws CustomException {
        List<HealthRecordEntity> healthRecordEntities = healthRecordRepository.findAll();
        if (healthRecordEntities.isEmpty()) {
            throw new CustomException(new ResponseMessageDto("No health records found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return healthRecordEntities
                .stream()
                .map(healthRecordEntity -> modelMapper.map(healthRecordEntity, HealthRecordDto.class))
                .toList();
    }

    @Override
    public List<HealthRecordDto> getPatientsHealthRecords(String patientId) throws CustomException {
        Optional<PatientEntity> patientEntity = patientRepository.findById(patientId);
        if (patientEntity.isEmpty() || !patientEntity.get().isActive()) {
            throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        List<HealthRecordEntity> healthRecordEntities = healthRecordRepository.findByPatientId(patientEntity.get().getPatientId());
        if (healthRecordEntities.isEmpty()) {
            throw new CustomException(new ResponseMessageDto("No health records found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        return healthRecordEntities
                .stream()
                .map(healthRecordEntity -> modelMapper.map(healthRecordEntity, HealthRecordDto.class))
                .toList();
    }

    @Override
    public void updateHealthRecord(HealthRecordDto healthRecordDto) throws CustomException {

    }

    @Override
    public void deleteHealthRecord(Long healthRecordId) throws CustomException {

    }
}
