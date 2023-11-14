package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.AvailabilitySlotRequestDto;
import com.shadril.doctorservice.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DoctorAvailabilityService {
    void createAvailabilitySlots(AvailabilitySlotRequestDto availabilitySlotRequestDto) throws CustomException;
}
