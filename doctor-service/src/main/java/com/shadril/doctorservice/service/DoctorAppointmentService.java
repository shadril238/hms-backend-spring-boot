package com.shadril.doctorservice.service;

import com.shadril.doctorservice.dto.*;
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
    List<DoctorAvailabilityDto> getDoctorAvailibilityByDoctorIdAndDate(String doctorId, String date) throws CustomException;
    void deleteDoctorAvailabilitySlot(Long doctorAvailabilityId) throws CustomException;
    List<AppointmentAllDataDto> getPatientsBookedAppointment(String patientId) throws CustomException;
    List<AppointmentDto> getDoctorBookedAppointmentByDate(String doctorId, String date) throws CustomException;
    Long countTotalAppointments() throws CustomException;
    List<AppointmentDto> patientUpcomingAppointments(String patientId) throws CustomException;
    List<AppointmentDto> todayAppointments(String doctorId) throws CustomException;
    List<AppointmentDto> doctorAllAppointments(String doctorId) throws CustomException;
}
