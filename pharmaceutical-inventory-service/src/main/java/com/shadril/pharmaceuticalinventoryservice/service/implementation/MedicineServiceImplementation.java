package com.shadril.pharmaceuticalinventoryservice.service.implementation;

import com.shadril.pharmaceuticalinventoryservice.dto.DoctorDto;
import com.shadril.pharmaceuticalinventoryservice.dto.MedicineDto;
import com.shadril.pharmaceuticalinventoryservice.dto.PatientDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicineAllocationEntity;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicineEntity;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.DoctorServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.PatientServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicineAllocationRepository;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicineRepository;
import com.shadril.pharmaceuticalinventoryservice.service.MedicineService;
import jakarta.transaction.Transactional;
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
@Transactional
public class MedicineServiceImplementation implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DoctorServiceFeignClient doctorServiceFeignClient;
    @Autowired
    private MedicineAllocationRepository medicineAllocationRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;


    @Override
    public void createMedicine(MedicineDto medicineDto) throws CustomException {
        try{
            MedicineEntity medicineEntity = modelMapper.map(medicineDto, MedicineEntity.class);
            medicineRepository.save(medicineEntity);
        } catch (Exception e){
            log.error("Error while creating medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while creating medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateMedicine(MedicineDto medicineDto) throws CustomException {
        try{
            Optional<MedicineEntity> existingMedicine = medicineRepository.findById(medicineDto.getMedicineId());
            if(existingMedicine.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicineEntity medicineEntity = existingMedicine.get();
            medicineEntity.setMedicineName(medicineDto.getMedicineName()==null?medicineEntity.getMedicineName():medicineDto.getMedicineName());
            medicineEntity.setGenericName(medicineDto.getGenericName()==null?medicineEntity.getGenericName():medicineDto.getGenericName());
            medicineEntity.setMedicineType(medicineDto.getMedicineType()==null?medicineEntity.getMedicineType():medicineDto.getMedicineType());
            medicineEntity.setManufacturer(medicineDto.getManufacturer()==null?medicineEntity.getManufacturer():medicineDto.getManufacturer());
            medicineEntity.setManufactureDate(medicineDto.getManufactureDate()==null?medicineEntity.getManufactureDate():medicineDto.getManufactureDate());
            medicineEntity.setExpiryDate(medicineDto.getExpiryDate()==null?medicineEntity.getExpiryDate():medicineDto.getExpiryDate());
            medicineEntity.setIsOccupied(medicineDto.getIsOccupied()==null?medicineEntity.getIsOccupied():medicineDto.getIsOccupied());
            medicineRepository.save(medicineEntity);
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while updating medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while updating medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteMedicine(Long id) throws CustomException {
        try {
            Optional<MedicineEntity> existingMedicine = medicineRepository.findById(id);
            if(existingMedicine.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicineRepository.deleteById(id);
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while deleting medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while deleting medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public MedicineDto getMedicine(Long id) throws CustomException {
        try {
            Optional<MedicineEntity> existingMedicine = medicineRepository.findById(id);
            if(existingMedicine.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return modelMapper.map(existingMedicine.get(), MedicineDto.class);
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while getting medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while getting medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicineDto> getAllMedicines() throws CustomException {
        try {
            List<MedicineEntity> medicineEntities = medicineRepository.findAll();
            if (medicineEntities.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No medicine found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicineEntities.stream().map(medicineEntity -> modelMapper.map(medicineEntity, MedicineDto.class)).toList();
        }
        catch (Exception e){
            log.error("Error while getting all medicines: ", e);
            throw new CustomException(new ResponseMessageDto("Error while getting all medicines", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseMessageDto bookMedicine(Long medicineId, String patientId) throws CustomException {
        try {
            Optional<MedicineEntity> medicineEntity = medicineRepository.findById(medicineId);
            if(medicineEntity.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicineEntity medicine = medicineEntity.get();
            if(medicine.getIsOccupied()){
                throw new CustomException(new ResponseMessageDto("Medicine is already occupied", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            DoctorDto doctorDto = doctorServiceFeignClient.getCurrentDoctor().getBody();
            if(doctorDto == null){
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            PatientDto patientDto = patientServiceFeignClient.getPatientById(patientId).getBody();
            if(patientDto == null){
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicine.setIsOccupied(true);
            medicineRepository.save(medicine);

            MedicineAllocationEntity medicineAllocationEntity = new MedicineAllocationEntity();
            medicineAllocationEntity.setMedicine(medicine);
            medicineAllocationEntity.setAllocatedDate(LocalDate.now());
            medicineAllocationEntity.setDoctorId(doctorDto.getDoctorId());
            medicineAllocationEntity.setPatientId(patientId);
            medicineAllocationRepository.save(medicineAllocationEntity);
            return new ResponseMessageDto("Medicine booked successfully", HttpStatus.OK);
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while booking medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while booking medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseMessageDto returnMedicine(Long medicineId, String patientId) throws CustomException {
        try{
            Optional<MedicineEntity> medicineEntity = medicineRepository.findById(medicineId);
            if(medicineEntity.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicineEntity medicine = medicineEntity.get();
            if(!medicine.getIsOccupied()){
                throw new CustomException(new ResponseMessageDto("Medicine is not occupied", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            DoctorDto doctorDto = doctorServiceFeignClient.getCurrentDoctor().getBody();
            if(doctorDto == null){
                throw new CustomException(new ResponseMessageDto("Doctor not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            PatientDto patientDto = patientServiceFeignClient.getPatientById(patientId).getBody();
            if(patientDto == null){
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            medicine.setIsOccupied(false);
            medicineRepository.save(medicine);

            Optional<MedicineAllocationEntity> medicineAllocationEntity = medicineAllocationRepository.findByMedicineAndPatientId(medicine, patientId);
            if(medicineAllocationEntity.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine allocation not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            MedicineAllocationEntity medicineAllocation = medicineAllocationEntity.get();
            medicineAllocation.setIsReturned(true);
            medicineAllocation.setReturnedDate(LocalDate.now());
            medicineAllocationRepository.save(medicineAllocation);
            return new ResponseMessageDto("Medicine returned successfully", HttpStatus.OK);
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while returning medicine: ", e);
            throw new CustomException(new ResponseMessageDto("Error while returning medicine", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

        }
    }

    @Override
    public List<MedicineDto> searchMedicineByName(String name) throws CustomException {
        try{
            List<MedicineEntity> medicineEntities = medicineRepository.findByMedicineNameContainingIgnoreCase(name);
            if(medicineEntities.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicineEntities.stream().map(medicineEntity -> modelMapper.map(medicineEntity, MedicineDto.class)).toList();
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while searching medicine by name: ", e);
            throw new CustomException(new ResponseMessageDto("Error while searching medicine by name", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicineDto> searchMedicineByManufacturer(String manufacturer) throws CustomException {
        try{
            List<MedicineEntity> medicineEntities = medicineRepository.findByManufacturerContainingIgnoreCase(manufacturer);
            if(medicineEntities.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicineEntities.stream().map(medicineEntity -> modelMapper.map(medicineEntity, MedicineDto.class)).toList();
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while searching medicine by manufacturer: ", e);
            throw new CustomException(new ResponseMessageDto("Error while searching medicine by manufacturer", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MedicineDto> searchMedicineByGenericName(String genericName) throws CustomException {
        try{
            List<MedicineEntity> medicineEntities = medicineRepository.findByGenericNameContainingIgnoreCase(genericName);
            if(medicineEntities.isEmpty()){
                throw new CustomException(new ResponseMessageDto("Medicine not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            return medicineEntities.stream().map(medicineEntity -> modelMapper.map(medicineEntity, MedicineDto.class)).toList();
        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error while searching medicine by generic name: ", e);
            throw new CustomException(new ResponseMessageDto("Error while searching medicine by generic name", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
