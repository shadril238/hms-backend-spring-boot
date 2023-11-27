package com.shadril.pharmaceuticalinventoryservice.service.implementation;

import com.shadril.pharmaceuticalinventoryservice.dto.DoctorDto;
import com.shadril.pharmaceuticalinventoryservice.dto.MedicalEquipmentDto;
import com.shadril.pharmaceuticalinventoryservice.dto.PatientDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentAllocationEntity;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentEntity;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.DoctorServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.PatientServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicalEquipmentAllocationRepository;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicalEquipmentRepository;
import com.shadril.pharmaceuticalinventoryservice.service.MedicalEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MedicalEquipmentServiceImplementation implements MedicalEquipmentService {
    @Autowired
    private MedicalEquipmentRepository medicalEquipmentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MedicalEquipmentAllocationRepository medicalEquipmentAllocationRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private DoctorServiceFeignClient doctorServiceFeignClient;

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

    @Override
    public ResponseMessageDto bookMedicalEquipment(Long medicalEquipmentId, String patientId) throws CustomException {
        try{
            Optional<MedicalEquipmentEntity> existingMedicalEquipment = medicalEquipmentRepository.findById(medicalEquipmentId);
            if (existingMedicalEquipment.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicalEquipmentEntity medicalEquipmentEntity = existingMedicalEquipment.get();
            if (medicalEquipmentEntity.getIsOccupied()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment is already occupied", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            PatientDto patientResponse = patientServiceFeignClient.getPatientById(patientId).getBody();
            if (patientResponse == null) {
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            DoctorDto doctorResponse = doctorServiceFeignClient.getCurrentDoctor().getBody();
            if (doctorResponse == null) {
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicalEquipmentEntity.setIsOccupied(true);
            medicalEquipmentRepository.save(medicalEquipmentEntity);

            MedicalEquipmentAllocationEntity medicalEquipmentAllocationEntity = new MedicalEquipmentAllocationEntity();
            medicalEquipmentAllocationEntity.setMedicalEquipment(medicalEquipmentEntity);
            medicalEquipmentAllocationEntity.setPatientId(patientId);
            medicalEquipmentAllocationEntity.setDoctorId(doctorResponse.getDoctorId());
            medicalEquipmentAllocationEntity.setAllocatedDate(LocalDate.now());
            medicalEquipmentAllocationEntity.setIsReturned(false);
            medicalEquipmentAllocationEntity.setIsActive(true);

            medicalEquipmentAllocationRepository.save(medicalEquipmentAllocationEntity);

            return new ResponseMessageDto("Medical equipment booked successfully", HttpStatus.OK);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while booking medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while booking medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseMessageDto returnMedicalEquipment(Long medicalEquipmentId, String patientId) throws CustomException {
        try{
            Optional<MedicalEquipmentEntity> existingMedicalEquipment = medicalEquipmentRepository.findById(medicalEquipmentId);
            if (existingMedicalEquipment.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicalEquipmentEntity medicalEquipmentEntity = existingMedicalEquipment.get();
            if (!medicalEquipmentEntity.getIsOccupied()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment is not occupied", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            PatientDto patientResponse = patientServiceFeignClient.getPatientById(patientId).getBody();
            if (patientResponse == null) {
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            DoctorDto doctorResponse = doctorServiceFeignClient.getCurrentDoctor().getBody();
            if (doctorResponse == null) {
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicalEquipmentEntity.setIsOccupied(false);
            medicalEquipmentRepository.save(medicalEquipmentEntity);

            Optional<MedicalEquipmentAllocationEntity> existingMedicalEquipmentAllocation = medicalEquipmentAllocationRepository.findByMedicalEquipmentAndPatientId(medicalEquipmentEntity, patientId);
            if (existingMedicalEquipmentAllocation.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Medical equipment allocation not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            MedicalEquipmentAllocationEntity medicalEquipmentAllocationEntity = existingMedicalEquipmentAllocation.get();
            medicalEquipmentAllocationEntity.setIsReturned(true);
            medicalEquipmentAllocationEntity.setReturnedDate(LocalDate.now());
            medicalEquipmentAllocationRepository.save(medicalEquipmentAllocationEntity);

            medicalEquipmentEntity.setIsOccupied(false);
            medicalEquipmentRepository.save(medicalEquipmentEntity);

            return new ResponseMessageDto("Medical equipment returned successfully", HttpStatus.OK);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while returning medical equipment: ", e);
            throw new CustomException(new ResponseMessageDto("Error while returning medical equipment", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicalEquipmentDto> searchMedicalEquipmentByName(String name) throws CustomException {
        try{
            List<MedicalEquipmentEntity> medicalEquipmentEntities = medicalEquipmentRepository.findByMedicalEquipmentName(name);
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
            log.error("Error while searching medical equipment by name: ", e);
            throw new CustomException(new ResponseMessageDto("Error while searching medical equipment by name", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicalEquipmentDto> searchMedicalEquipmentByManufacturer(String manufacturer) throws CustomException {
        try{
            List<MedicalEquipmentEntity> medicalEquipmentEntities = medicalEquipmentRepository.findByManufacturer(manufacturer);
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
            log.error("Error while searching medical equipment by manufacturer: ", e);
            throw new CustomException(new ResponseMessageDto("Error while searching medical equipment by manufacturer", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicalEquipmentDto> alertMedicalEquipmentExpiry() throws CustomException {
        try {
            List<MedicalEquipmentEntity> medicalEquipmentEntities = medicalEquipmentRepository.findAll();
            if (medicalEquipmentEntities.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No medical equipment found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicalEquipmentEntities
                    .stream()
                    .filter(medicalEquipmentEntity -> medicalEquipmentEntity.getExpiryDate().isBefore(LocalDate.now().plusDays(7)))
                    .map(medicalEquipmentEntity -> modelMapper.map(medicalEquipmentEntity, MedicalEquipmentDto.class))
                    .toList();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while alerting medical equipment expiry: ", e);
            throw new CustomException(new ResponseMessageDto("Error while alerting medical equipment expiry", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
