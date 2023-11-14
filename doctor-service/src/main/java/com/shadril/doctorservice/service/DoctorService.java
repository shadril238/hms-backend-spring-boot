package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.DoctorDto;
import com.shadril.doctorservice.dto.DoctorRegistrationRequestDto;
import com.shadril.doctorservice.exception.CustomException;

public interface DoctorService {
    DoctorDto registerDoctor(DoctorRegistrationRequestDto doctorRegistrationRequestDto) throws CustomException;
    DoctorDto getDoctorById(String doctorId) throws CustomException;
    DoctorDto getDoctorByEmail(String email) throws CustomException;
    DoctorDto getCurrentDoctor() throws CustomException;
    void updateDoctorProfile(DoctorDto doctorDto) throws CustomException;
}
