package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.AppointmentSlotRequestDto;
import com.shadril.doctorservice.dto.BookAppointmentRequestDto;
import com.shadril.doctorservice.exception.CustomException;

public interface DoctorAppointmentService {
    void createAppointmentSlot(AppointmentSlotRequestDto appointmentSlotRequestDto) throws CustomException;
    void bookAppointmentSlot(BookAppointmentRequestDto bookAppointmentRequestDto) throws CustomException;
}
