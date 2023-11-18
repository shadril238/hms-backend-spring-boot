package com.shadril.pharmaceuticalinventoryservice.service.implementation;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicineDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicineEntity;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicineRepository;
import com.shadril.pharmaceuticalinventoryservice.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MedicineServiceImplementation implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ModelMapper modelMapper;


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
}
