package com.shadril.pharmaceuticalinventoryservice.service;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicalEquipmentDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;

import java.util.List;

public interface MedicalEquipmentService {
    void createMedicalEquipment(MedicalEquipmentDto medicalEquipmentDto) throws CustomException;
    void updateMedicalEquipment(MedicalEquipmentDto medicalEquipmentDto) throws CustomException;
    void deleteMedicalEquipment(Long id) throws CustomException;
    MedicalEquipmentDto getMedicalEquipment(Long id) throws CustomException;
    List<MedicalEquipmentDto> getAllMedicalEquipments() throws CustomException;
    ResponseMessageDto bookMedicalEquipment(Long medicalEquipmentId, String patientId) throws CustomException;
    ResponseMessageDto returnMedicalEquipment(Long medicalEquipmentId, String patientId) throws CustomException;
}
