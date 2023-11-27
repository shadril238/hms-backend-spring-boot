package com.shadril.pharmaceuticalinventoryservice.controller;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicalEquipmentDto;
import com.shadril.pharmaceuticalinventoryservice.dto.MedicineDto;
import com.shadril.pharmaceuticalinventoryservice.dto.ResponseMessageDto;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.service.MedicalEquipmentService;
import com.shadril.pharmaceuticalinventoryservice.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/pharmaceutical-inventory")
public class PharmaceuticalInventoryController {
    @Autowired
    private MedicalEquipmentService medicalEquipmentService;
    @Autowired
    private MedicineService medicineService;

    @PostMapping("/medical-equipment/create")
    public ResponseEntity<ResponseMessageDto> createMedicalEquipment(@RequestBody MedicalEquipmentDto medicalEquipmentDto)
            throws CustomException {
        medicalEquipmentService.createMedicalEquipment(medicalEquipmentDto);
        return new ResponseEntity<>(new ResponseMessageDto("Medical equipment created successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/medical-equipment/update")
    public ResponseEntity<ResponseMessageDto> updateMedicalEquipment(@RequestBody MedicalEquipmentDto medicalEquipmentDto)
            throws CustomException {
        medicalEquipmentService.updateMedicalEquipment(medicalEquipmentDto);
        return new ResponseEntity<>(new ResponseMessageDto("Medical equipment updated successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/medical-equipment/delete/{id}")
    public ResponseEntity<ResponseMessageDto> deleteMedicalEquipment(@PathVariable Long id) throws CustomException {
        medicalEquipmentService.deleteMedicalEquipment(id);
        return new ResponseEntity<>(new ResponseMessageDto("Medical equipment deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/medical-equipment/get/{id}")
    public ResponseEntity<MedicalEquipmentDto> getMedicalEquipment(@PathVariable Long id) throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.getMedicalEquipment(id), HttpStatus.OK);
    }

    @GetMapping("/medical-equipment/get-all")
    public ResponseEntity<Iterable<MedicalEquipmentDto>> getAllMedicalEquipments() throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.getAllMedicalEquipments(), HttpStatus.OK);
    }

    @PostMapping("/medical-equipment/book/{medicalEquipmentId}/{patientId}")
    public ResponseEntity<ResponseMessageDto> bookMedicalEquipment(@PathVariable Long medicalEquipmentId, @PathVariable String patientId)
            throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.bookMedicalEquipment(medicalEquipmentId, patientId), HttpStatus.OK);
    }

    @PostMapping("/medical-equipment/return/{medicalEquipmentId}/{patientId}")
    public ResponseEntity<ResponseMessageDto> returnMedicalEquipment(@PathVariable Long medicalEquipmentId, @PathVariable String patientId)
            throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.returnMedicalEquipment(medicalEquipmentId, patientId), HttpStatus.OK);
    }

    @PostMapping("/medicine/create")
    public ResponseEntity<ResponseMessageDto> createMedicine(@RequestBody MedicineDto medicineDto)
            throws CustomException {
        medicineService.createMedicine(medicineDto);
        return new ResponseEntity<>(new ResponseMessageDto("Medicine created successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/medicine/update")
    public ResponseEntity<ResponseMessageDto> updateMedicine(@RequestBody MedicineDto medicineDto)
            throws CustomException {
        medicineService.updateMedicine(medicineDto);
        return new ResponseEntity<>(new ResponseMessageDto("Medicine updated successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/medicine/delete/{id}")
    public ResponseEntity<ResponseMessageDto> deleteMedicine(@PathVariable Long id) throws CustomException {
        medicineService.deleteMedicine(id);
        return new ResponseEntity<>(new ResponseMessageDto("Medicine deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/medicine/get/{id}")
    public ResponseEntity<MedicineDto> getMedicine(@PathVariable Long id) throws CustomException {
        return new ResponseEntity<>(medicineService.getMedicine(id), HttpStatus.OK);
    }

    @GetMapping("/medicine/get-all")
    public ResponseEntity<Iterable<MedicineDto>> getAllMedicines() throws CustomException {
        return new ResponseEntity<>(medicineService.getAllMedicines(), HttpStatus.OK);
    }

    @PostMapping("/medicine/book/{medicineId}/{patientId}")
    public ResponseEntity<ResponseMessageDto> bookMedicine(@PathVariable Long medicineId, @PathVariable String patientId)
            throws CustomException {
        return new ResponseEntity<>(medicineService.bookMedicine(medicineId, patientId), HttpStatus.OK);
    }

    @PostMapping("/medicine/return/{medicineId}/{patientId}")
    public ResponseEntity<ResponseMessageDto> returnMedicine(@PathVariable Long medicineId, @PathVariable String patientId)
            throws CustomException {
        return new ResponseEntity<>(medicineService.returnMedicine(medicineId, patientId), HttpStatus.OK);
    }

    @GetMapping("/medicine/search/name/{name}")
    public ResponseEntity<Iterable<MedicineDto>> searchMedicineByName(@PathVariable String name) throws CustomException {
        return new ResponseEntity<>(medicineService.searchMedicineByName(name), HttpStatus.OK);
    }

    @GetMapping("/medicine/search/manufacturer/{manufacturer}")
    public ResponseEntity<Iterable<MedicineDto>> searchMedicineByManufacturer(@PathVariable String manufacturer) throws CustomException {
        return new ResponseEntity<>(medicineService.searchMedicineByManufacturer(manufacturer), HttpStatus.OK);
    }

    @GetMapping("/medicine/search/generic-name/{genericName}")
    public ResponseEntity<Iterable<MedicineDto>> searchMedicineByGenericName(@PathVariable String genericName) throws CustomException {
        return new ResponseEntity<>(medicineService.searchMedicineByGenericName(genericName), HttpStatus.OK);
    }

    @GetMapping("/medical-equipment/search/name/{name}")
    public ResponseEntity<Iterable<MedicalEquipmentDto>> searchMedicalEquipmentByName(@PathVariable String name) throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.searchMedicalEquipmentByName(name), HttpStatus.OK);
    }

    @GetMapping("/medical-equipment/search/manufacturer/{manufacturer}")
    public ResponseEntity<Iterable<MedicalEquipmentDto>> searchMedicalEquipmentByManufacturer(@PathVariable String manufacturer) throws CustomException {
        return new ResponseEntity<>(medicalEquipmentService.searchMedicalEquipmentByManufacturer(manufacturer), HttpStatus.OK);
    }
}
