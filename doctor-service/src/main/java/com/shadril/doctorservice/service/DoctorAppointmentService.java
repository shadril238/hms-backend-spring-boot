package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.AppointmentSlotRequestDto;
import com.shadril.doctorservice.exception.CustomException;

public interface DoctorAppointmentService {
    void createAppointmentSlot(AppointmentSlotRequestDto appointmentSlotRequestDto) throws CustomException;
}
