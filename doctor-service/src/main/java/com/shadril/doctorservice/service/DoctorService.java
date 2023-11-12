package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.DoctorDto;
import com.shadril.doctorservice.dto.DoctorRegistrationRequestDto;
import com.shadril.doctorservice.exception.CustomException;

public interface DoctorService {
    DoctorDto registerDoctor(DoctorRegistrationRequestDto doctorRegistrationRequestDto) throws CustomException;
    DoctorDto getDoctorById(String doctorId) throws CustomException;
}
