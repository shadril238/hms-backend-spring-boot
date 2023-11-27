package com.shadril.pharmaceuticalinventoryservice.service;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicineDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;

import java.util.List;

public interface MedicineService {
    void createMedicine(MedicineDto medicineDto) throws CustomException;
    void updateMedicine(MedicineDto medicineDto) throws CustomException;
    void deleteMedicine(Long id) throws CustomException;
    MedicineDto getMedicine(Long id) throws CustomException;
    List<MedicineDto> getAllMedicines() throws CustomException;
    ResponseMessageDto bookMedicine(Long medicineId, String patientId) throws CustomException;
    ResponseMessageDto returnMedicine(Long medicineId, String patientId) throws CustomException;
    List<MedicineDto> searchMedicineByName(String name) throws CustomException;
    List<MedicineDto> searchMedicineByManufacturer(String manufacturer) throws CustomException;
    List<MedicineDto> searchMedicineByGenericName(String genericName) throws CustomException;
}
