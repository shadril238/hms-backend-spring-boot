package com.shadril.patientservice;

import com.shadril.patientservice.exception.CustomException;
import com.shadril.patientservice.repository.PatientRepository;
import com.shadril.patientservice.service.implementation.PatientServiceImplementation;
import com.shadril.patientservice.dto.PatientDto;
import com.shadril.patientservice.entity.PatientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplementationTest {

    @Mock
    private PatientRepository patientRepository;

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
}
