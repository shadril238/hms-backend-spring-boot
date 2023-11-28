package com.shadril.patientservice;

import com.shadril.patientservice.dto.PatientRegistrationRequestDto;
import com.shadril.patientservice.dto.UserRegistrationResponseDto;
import com.shadril.patientservice.enums.BloodGroup;
import com.shadril.patientservice.enums.Gender;
import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.networkmanager.SecurityServiceFeignClient;
import com.shadril.patientservice.repository.PatientRepository;
import com.shadril.patientservice.service.implementation.PatientServiceImplementation;
import com.shadril.patientservice.dto.PatientDto;
import com.shadril.patientservice.entity.PatientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplementationTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SecurityServiceFeignClient securityServiceFeignClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatientServiceImplementation patientService;

    @Test
    public void getAllPatientsTest() throws CustomException {
        PatientEntity patient1 = new PatientEntity();
        PatientEntity patient2 = new PatientEntity();
        List<PatientEntity> patientEntities = Arrays.asList(patient1, patient2);

        PatientDto patientDto1 = new PatientDto();
        PatientDto patientDto2 = new PatientDto();
        List<PatientDto> expectedPatientDtos = Arrays.asList(patientDto1, patientDto2);

        when(patientRepository.findAll()).thenReturn(patientEntities);
        when(modelMapper.map(patient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(patient2, PatientDto.class)).thenReturn(patientDto2);

        List<PatientDto> actualPatientDtos = patientService.getAllPatients();

        assertEquals(expectedPatientDtos, actualPatientDtos, "The returned list of patients should match the expected list.");
    }

    @Test
    public void getPatientByIdSuccessTest() throws CustomException {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setPatientId("123");
        patientEntity.setActive(true);

        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId("123");

        when(patientRepository.findById(Mockito.anyString())).thenReturn(Optional.of(patientEntity));

        when(modelMapper.map(patientEntity, PatientDto.class)).thenReturn(patientDto);

        PatientDto retrievedPatient = patientService.getPatientById("123");

        assertEquals(patientDto, retrievedPatient);
    }

    @Test
    public void approvePatientSuccessTest() throws CustomException {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setPatientId("123");
        patientEntity.setActive(true);
        patientEntity.setApproved(false);

        when(patientRepository.findById(Mockito.anyString())).thenReturn(Optional.of(patientEntity));

        patientService.approvePatient("123");
        assertEquals(true, patientEntity.isApproved());
    }
}
