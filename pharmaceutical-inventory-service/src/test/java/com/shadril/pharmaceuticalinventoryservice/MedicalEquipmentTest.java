package com.shadril.pharmaceuticalinventoryservice;

import com.shadril.pharmaceuticalinventoryservice.dto.MedicalEquipmentDto;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentEntity;
import com.shadril.pharmaceuticalinventoryservice.exception.CustomException;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.DoctorServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.networkmanager.PatientServiceFeignClient;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicalEquipmentAllocationRepository;
import com.shadril.pharmaceuticalinventoryservice.repository.MedicalEquipmentRepository;
import com.shadril.pharmaceuticalinventoryservice.service.implementation.MedicalEquipmentServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class MedicalEquipmentTest {
    @InjectMocks
    private MedicalEquipmentServiceImplementation medicalEquipmentService;

    @Mock
    private MedicalEquipmentRepository medicalEquipmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MedicalEquipmentAllocationRepository medicalEquipmentAllocationRepository;

    @Mock
    private PatientServiceFeignClient patientServiceFeignClient;

    @Mock
    private DoctorServiceFeignClient doctorServiceFeignClient;



}
