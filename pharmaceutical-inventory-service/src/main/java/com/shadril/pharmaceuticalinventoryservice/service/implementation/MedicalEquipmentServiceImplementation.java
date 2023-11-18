package com.shadril.pharmaceuticalinventoryservice.service.implementation;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicalEquipmentDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentEntity;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicalEquipmentRepository;
import com.shadril.pharmaceuticalinventoryservice.service.MedicalEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MedicalEquipmentServiceImplementation implements MedicalEquipmentService {
    @Autowired
    private MedicalEquipmentRepository medicalEquipmentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createMedicalEquipment(MedicalEquipmentDto medicalEquipmentDto) throws CustomException {
        try {
            MedicalEquipmentEntity medicalEquipmentEntity = modelMapper.map(medicalEquipmentDto, MedicalEquipmentEntity.class);
            medicalEquipmentRepository.save(medicalEquipmentEntity);
        } catch (Exception e) {
            log.error("Error while creating medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while creating medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateMedicalEquipment(MedicalEquipmentDto medicalEquipmentDto) throws CustomException {
        try {
            Optional<MedicalEquipmentEntity> existingMedicalEquipment = medicalEquipmentRepository.findById(medicalEquipmentDto.getMedicalEquipmentId());
            if (existingMedicalEquipment.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicalEquipmentEntity medicalEquipmentEntity = existingMedicalEquipment.get();
            medicalEquipmentEntity.setMedicalEquipmentName(medicalEquipmentDto.getMedicalEquipmentName() == null ? medicalEquipmentEntity.getMedicalEquipmentName() : medicalEquipmentDto.getMedicalEquipmentName());
            medicalEquipmentEntity.setMedicalEquipmentType(medicalEquipmentDto.getMedicalEquipmentType() == null ? medicalEquipmentEntity.getMedicalEquipmentType() : medicalEquipmentDto.getMedicalEquipmentType());
            medicalEquipmentEntity.setManufacturer(medicalEquipmentDto.getManufacturer() == null ? medicalEquipmentEntity.getManufacturer() : medicalEquipmentDto.getManufacturer());
            medicalEquipmentEntity.setManufactureDate(medicalEquipmentDto.getManufactureDate() == null ? medicalEquipmentEntity.getManufactureDate() : medicalEquipmentDto.getManufactureDate());
            medicalEquipmentEntity.setExpiryDate(medicalEquipmentDto.getExpiryDate() == null ? medicalEquipmentEntity.getExpiryDate() : medicalEquipmentDto.getExpiryDate());
            medicalEquipmentEntity.setIsOccupied(medicalEquipmentDto.getIsOccupied() == null ? medicalEquipmentEntity.getIsOccupied() : medicalEquipmentDto.getIsOccupied());
            medicalEquipmentRepository.save(medicalEquipmentEntity);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while updating medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while updating medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteMedicalEquipment(Long id) throws CustomException {
        try {
            Optional<MedicalEquipmentEntity> existingMedicalEquipment = medicalEquipmentRepository.findById(id);
            if (existingMedicalEquipment.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicalEquipmentRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while deleting medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while deleting medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public MedicalEquipmentDto getMedicalEquipment(Long id) throws CustomException {
        try {
            Optional<MedicalEquipmentEntity> existingMedicalEquipment = medicalEquipmentRepository.findById(id);
            if (existingMedicalEquipment.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicalEquipmentEntity medicalEquipmentEntity = existingMedicalEquipment.get();
            return modelMapper.map(medicalEquipmentEntity, MedicalEquipmentDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while getting medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while getting medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicalEquipmentDto> getAllMedicalEquipments() throws CustomException {
        try {
            List<MedicalEquipmentEntity> medicalEquipmentEntities = medicalEquipmentRepository.findAll();
            if (medicalEquipmentEntities.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No medical equipment found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicalEquipmentEntities
                    .stream()
                    .map(medicalEquipmentEntity -> modelMapper.map(medicalEquipmentEntity, MedicalEquipmentDto.class))
                    .toList();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while getting all medical equipments: ", e);
            throw new CustomException(new ResponseMessageDto("Error while getting all medical equipments", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
