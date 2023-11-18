package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.AppointmentDto;
import com.shadril.doctorservice.dto.AppointmentSlotRequestDto;
import com.shadril.doctorservice.dto.BookAppointmentRequestDto;
import com.shadril.doctorservice.exception.CustomException;

import java.util.List;

public interface DoctorAppointmentService {
    void createAppointmentSlot(AppointmentSlotRequestDto appointmentSlotRequestDto) throws CustomException;
    void bookAppointmentSlot(BookAppointmentRequestDto bookAppointmentRequestDto) throws CustomException;
    void deleteAppointmentSlot(Long appointmentSlotId) throws CustomException;
    void cancelAppointment(Long appointmentId) throws CustomException;
    AppointmentDto getAppointmentById(String appointmentId) throws CustomException;
    AppointmentDto getAppointmentByAppointmentSlotId(String appointmentSlotId) throws CustomException;
    AppointmentDto getAppointmentByPatientId(String patientId) throws CustomException;
    List<AppointmentDto> getAppointmentByDoctorId(String doctorId) throws CustomException;
    List<AppointmentDto> getAppointmentByDoctorIdAndDate(String doctorId, String date) throws CustomException;
    List<AppointmentDto> getBookedAppointmentByDoctorId(String doctorId) throws CustomException;
    List<AppointmentDto> getBookedAppointmentByDoctorIdAndDate(String doctorId, String date) throws CustomException;
}
