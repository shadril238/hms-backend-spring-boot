package com.shadril.patientservice;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testSearchPatientByName() throws CustomException {
        String searchName = "TestName";
        PatientEntity patient1 = new PatientEntity();
        PatientEntity patient2 = new PatientEntity();
        List<PatientEntity> patientEntities = Arrays.asList(patient1, patient2);

        PatientDto patientDto1 = new PatientDto();
        PatientDto patientDto2 = new PatientDto();

        when(patientRepository.searchByFirstNameOrLastNameContainingIgnoreCase(searchName))
                .thenReturn(patientEntities);
        when(modelMapper.map(patient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(patient2, PatientDto.class)).thenReturn(patientDto2);

        List<PatientDto> result = patientService.searchPatientByName(searchName);

        assertEquals(2, result.size());
        assertTrue(result.contains(patientDto1));
        assertTrue(result.contains(patientDto2));
        verify(patientRepository).searchByFirstNameOrLastNameContainingIgnoreCase(searchName);
        verify(modelMapper, times(2)).map(any(PatientEntity.class), eq(PatientDto.class));
    }

    @Test
    public void testGetAllApprovedPatients() throws CustomException {
        PatientEntity approvedPatient1 = new PatientEntity();
        PatientEntity approvedPatient2 = new PatientEntity();
        List<PatientEntity> approvedPatients = Arrays.asList(approvedPatient1, approvedPatient2);

        PatientDto patientDto1 = new PatientDto();
        PatientDto patientDto2 = new PatientDto();

        when(patientRepository.findAllByApproved(true)).thenReturn(approvedPatients);
        when(modelMapper.map(approvedPatient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(approvedPatient2, PatientDto.class)).thenReturn(patientDto2);

        List<PatientDto> result = patientService.getAllApprovedPatients();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(patientDto1, patientDto2)));
        verify(patientRepository).findAllByApproved(true);
        verify(modelMapper, times(2)).map(any(PatientEntity.class), eq(PatientDto.class));
    }

    @Test
    public void testGetAllUnapprovedPatients() throws CustomException {
        PatientEntity unapprovedPatient1 = new PatientEntity();
        PatientEntity unapprovedPatient2 = new PatientEntity();
        List<PatientEntity> unapprovedPatients = Arrays.asList(unapprovedPatient1, unapprovedPatient2);

        PatientDto patientDto1 = new PatientDto();
        PatientDto patientDto2 = new PatientDto();

        when(patientRepository.findAllByApproved(false)).thenReturn(unapprovedPatients);
        when(modelMapper.map(unapprovedPatient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(unapprovedPatient2, PatientDto.class)).thenReturn(patientDto2);


        List<PatientDto> result = patientService.getAllUnapprovedPatients();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(patientDto1, patientDto2)));
        verify(patientRepository).findAllByApproved(false);
        verify(modelMapper, times(2)).map(any(PatientEntity.class), eq(PatientDto.class));
    }

    @Test
    public void testCountTotalPatients() throws CustomException {
        long expectedCount = 10L;
        when(patientRepository.count()).thenReturn(expectedCount);

        long actualCount = patientService.countTotalPatients();

        assertEquals(expectedCount, actualCount);
        verify(patientRepository).count();
    }

    @Test
    public void testGetPatientByIdNotFound() {
        String patientId = "notFoundId";
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> patientService.getPatientById(patientId));
        verify(patientRepository).findById(patientId);
    }
}
