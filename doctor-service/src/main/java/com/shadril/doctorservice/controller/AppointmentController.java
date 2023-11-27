package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.*;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.DoctorAppointmentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private DoctorAppointmentService doctorAppointmentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessageDto> createAppointmentSlots(@RequestBody AppointmentSlotRequestDto slotRequest)
            throws CustomException {
        log.info("Received request to create appointment slots");
        doctorAppointmentService.createAppointmentSlot(slotRequest);
        log.info("Appointment slots created successfully");

        ResponseMessageDto responseMessageDto = new ResponseMessageDto("Appointments created successfully", HttpStatus.CREATED);

        return new ResponseEntity<>(responseMessageDto, HttpStatus.CREATED);
    }

    @PostMapping("/book")
    public ResponseEntity<ResponseMessageDto> bookAppointmentSlot(@RequestBody BookAppointmentRequestDto bookAppointment)
            throws CustomException {
        log.info("Received request to book appointment slots");
        doctorAppointmentService.bookAppointmentSlot(bookAppointment);
        log.info("Appointment slots booked successfully");

        ResponseMessageDto responseMessageDto = new ResponseMessageDto("Appointments booked successfully", HttpStatus.CREATED);

        return new ResponseEntity<>(responseMessageDto, HttpStatus.CREATED);
    }

    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable String appointmentId)
            throws CustomException {
        log.info("Received request to get appointment by id");
        AppointmentDto appointmentDto = doctorAppointmentService.getAppointmentById(appointmentId);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @GetMapping("/get/patient/{patientId}")
    public ResponseEntity<AppointmentDto> getAppointmentByPatientId(@PathVariable String patientId)
            throws CustomException {
        log.info("Received request to get appointment by patient id");
        AppointmentDto appointmentDto = doctorAppointmentService.getAppointmentByPatientId(patientId);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @GetMapping("/get/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByDoctorId(@PathVariable String doctorId)
            throws CustomException {
        log.info("Received request to get appointment by doctor id");
        List<AppointmentDto> appointmentList = doctorAppointmentService.getAppointmentByDoctorId(doctorId);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @GetMapping("/get/doctor/{doctorId}/{date}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByDoctorIdAndDate(@PathVariable String doctorId, @PathVariable String date)
            throws CustomException {
        log.info("Received request to get appointment by doctor id and date");
        List<AppointmentDto> appointmentList = doctorAppointmentService.getAppointmentByDoctorIdAndDate(doctorId, date);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @GetMapping("/get/booked/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getBookedAppointmentByDoctorId(@PathVariable String doctorId)
            throws CustomException {
        log.info("Received request to get booked appointment by doctor id");
        List<AppointmentDto> appointmentList = doctorAppointmentService.getBookedAppointmentByDoctorId(doctorId);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @GetMapping("/slots/get/doctor/{doctorId}/{date}")
    public ResponseEntity<List<DoctorAvailabilityDto>> getDoctorAvailibilityByDoctorIdAndDate(@PathVariable String doctorId, @PathVariable String date)
            throws CustomException {
        log.info("Received request to get doctor availability by doctor id and date");
        List<DoctorAvailabilityDto> doctorAvailabilityList = doctorAppointmentService.getDoctorAvailibilityByDoctorIdAndDate(doctorId, date);
        log.info("Doctor availability fetched successfully");
        return new ResponseEntity<>(doctorAvailabilityList, HttpStatus.OK);
    }

    @DeleteMapping("/slots/delete/{doctorAvailabilityId}")
    public ResponseEntity<ResponseMessageDto> deleteDoctorAvailabilitySlot(@PathVariable Long doctorAvailabilityId)
            throws CustomException {
        log.info("Received request to delete doctor availability slot");
        doctorAppointmentService.deleteDoctorAvailabilitySlot(doctorAvailabilityId);
        log.info("Doctor availability slot deleted successfully");

        ResponseMessageDto responseMessageDto = new ResponseMessageDto("Doctor availability slot deleted successfully", HttpStatus.OK);

        return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
    }

    @GetMapping("/get/all/patient/{patientId}")
    public ResponseEntity<List<AppointmentAllDataDto>> getPatientsBookedAppointment(@PathVariable String patientId)
            throws CustomException {
        log.info("Received request to get all appointments of patient");
        List<AppointmentAllDataDto> appointmentList = doctorAppointmentService.getPatientsBookedAppointment(patientId);
        log.info("Appointments fetched successfully");
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @GetMapping("/get/booked/doctor/{doctorId}/{date}")
    public ResponseEntity<List<AppointmentDto>> getDoctorBookedAppointmentByDate(@PathVariable String doctorId, @PathVariable String date)
            throws CustomException {
        log.info("Received request to get booked appointment by doctor id and date");
        List<AppointmentDto> appointmentList = doctorAppointmentService.getDoctorBookedAppointmentByDate(doctorId, date);
        log.info("Appointment fetched successfully");
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTotalAppointments()
            throws CustomException {
        log.info("Received request to count total appointments");
        Long count = doctorAppointmentService.countTotalAppointments();
        log.info("Total appointments counted successfully");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
