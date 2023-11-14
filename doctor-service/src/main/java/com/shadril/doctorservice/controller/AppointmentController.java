package com.shadril.doctorservice.controller;

import com.shadril.doctorservice.dto.AppointmentSlotRequestDto;
import com.shadril.doctorservice.dto.ResponseMessageDto;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.service.DoctorAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
