package com.shadril.pharmaceuticalinventoryservice.service;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicineDto;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;

import java.util.List;

public interface MedicineService {
    void createMedicine(MedicineDto medicineDto) throws CustomException;
    void updateMedicine(MedicineDto medicineDto) throws CustomException;
    void deleteMedicine(Long id) throws CustomException;
    MedicineDto getMedicine(Long id) throws CustomException;
    List<MedicineDto> getAllMedicines() throws CustomException;
}
