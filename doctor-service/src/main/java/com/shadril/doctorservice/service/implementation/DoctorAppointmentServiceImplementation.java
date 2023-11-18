package com.shadril.doctorservice.service.implementation;

import com.shadril.doctorservice.dto.*;
import com.shadril.doctorservice.entitiy.AppointmentEntity;
import com.shadril.doctorservice.entitiy.DoctorAvailabilityEntity;
import com.shadril.doctorservice.entitiy.DoctorEntity;
import com.shadril.doctorservice.enums.AppointmentStatus;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.networkmanager.PatientServiceFeignClient;
import com.shadril.doctorservice.repository.DoctorAppointmentRepository;
import com.shadril.doctorservice.repository.DoctorAvailabilityRepository;
import com.shadril.doctorservice.service.DoctorAppointmentService;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DoctorAppointmentServiceImplementation implements DoctorAppointmentService {
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;
    @Autowired
    private final PatientServiceFeignClient patientServiceFeignClient;

    @Override
    public void createAppointmentSlot(AppointmentSlotRequestDto requestDto)
            throws CustomException {
        try {
            log.info("inside createAppointmentSlots method in DoctorAppointmentServiceImplementation");
            DoctorDto doctorDto = doctorService.getCurrentDoctor();
            log.info("Doctor found with id: {}", doctorDto.getDoctorId());

            if (isSlotOverlapping(requestDto)) {
                throw new CustomException(new ResponseMessageDto("Time slot for appointment overlaps with existing slot", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            DoctorEntity doctorEntity = modelMapper.map(doctorDto, DoctorEntity.class);
            List<DoctorAvailabilityEntity> availabilitySlots = new ArrayList<>();
            LocalTime slotTime = requestDto.getStartTime();
            Long perPatientTime = requestDto.getPerPatientTimeInMinutes();
            LocalTime endTime = requestDto.getEndTime();

            if (Duration.between(slotTime, endTime).toMinutes() < perPatientTime) {
                throw new CustomException(new ResponseMessageDto("Per patient time exceeds the slot window", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            while (slotTime.isBefore(endTime)) {
                LocalTime slotEndTime = slotTime.plus(Duration.ofMinutes(perPatientTime));
                if (!slotEndTime.isAfter(requestDto.getEndTime())) {
                    DoctorAvailabilityEntity slot = new DoctorAvailabilityEntity();
                    slot.setDoctor(doctorEntity);
                    slot.setDate(LocalDate.from(requestDto.getDate().atTime(slotTime)));
                    slot.setStartTime(slotTime);
                    slot.setEndTime(slotEndTime);
                    slot.setIsAvailable(true);

                    availabilitySlots.add(slot);
                } else {
                    // if the slotEndTime is after the endTime, don't create this slot
                    break;
                }
                slotTime = slotEndTime;
            }
            doctorAvailabilityRepository.saveAll(availabilitySlots);
        } catch (CustomException ex) {
            log.error("Error occurred while creating availability slots: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while creating availability slots: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while creating availability slots", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void bookAppointmentSlot(BookAppointmentRequestDto bookAppointmentRequestDto) throws CustomException {
        try{
            log.info("inside bookAppointmentSlot method in DoctorAppointmentServiceImplementation");
            ResponseEntity<PatientDto> patientDto = patientServiceFeignClient.getPatientById(bookAppointmentRequestDto.getPatientId());
            if (patientDto.getStatusCode() != HttpStatus.OK || patientDto.getBody() == null || !patientDto.getBody().isActive()) {
                throw new CustomException(new ResponseMessageDto("Patient not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Patient found with id: {}", bookAppointmentRequestDto.getPatientId());

            Optional<DoctorAvailabilityEntity> appointmentSlotOptional = doctorAvailabilityRepository.findById(bookAppointmentRequestDto.getAvailabilityId());
            if (appointmentSlotOptional.isEmpty() || !appointmentSlotOptional.get().getIsAvailable()) {
                throw new CustomException(new ResponseMessageDto("Appointment slot not available", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment slot found with id: {}", bookAppointmentRequestDto.getAvailabilityId());
            DoctorAvailabilityEntity appointmentSlot = appointmentSlotOptional.get();

            appointmentSlot.setIsAvailable(false);
            doctorAvailabilityRepository.save(appointmentSlot);
            log.info("Appointment slot booked successfully");

            AppointmentEntity appointment = new AppointmentEntity();
            appointment.setDoctor(appointmentSlot.getDoctor());
            appointment.setPatientId(bookAppointmentRequestDto.getPatientId());
            appointment.setDoctorAvailability(appointmentSlot);
            appointment.setAppointmentType(bookAppointmentRequestDto.getAppointmentType());
            appointment.setAppointmentStatus(bookAppointmentRequestDto.getAppointmentStatus());
            appointment.setIsActive(true);
            doctorAppointmentRepository.save(appointment);
            log.info("Appointment booked successfully");

        } catch (CustomException ex) {
            log.error("Error occurred while booking appointment slot: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while booking appointment slot: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while booking appointment slot", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAppointmentSlot(Long appointmentSlotId) throws CustomException {
        try {
            log.info("inside deleteAppointmentSlot method in DoctorAppointmentServiceImplementation");
            Optional<DoctorAvailabilityEntity> appointmentSlotOptional = doctorAvailabilityRepository.findById(appointmentSlotId);
            if (appointmentSlotOptional.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment slot not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment slot found with id: {}", appointmentSlotId);
            DoctorAvailabilityEntity appointmentSlot = appointmentSlotOptional.get();

            appointmentSlot.setIsAvailable(false);
            doctorAvailabilityRepository.save(appointmentSlot);
            log.info("Appointment slot deleted successfully");
        } catch (CustomException ex) {
            log.error("Error occurred while deleting appointment slot: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while deleting appointment slot: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while deleting appointment slot", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws CustomException {
        try{
            log.info("inside cancelAppointment method in DoctorAppointmentServiceImplementation");
            Optional<AppointmentEntity> appointmentOptional = doctorAppointmentRepository.findById(appointmentId);
            if (appointmentOptional.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with id: {}", appointmentId);

            AppointmentEntity appointment = appointmentOptional.get();
            appointment.setIsActive(false);
            doctorAppointmentRepository.save(appointment);

            DoctorAvailabilityEntity appointmentSlot = appointment.getDoctorAvailability();
            appointmentSlot.setIsAvailable(true);
            doctorAvailabilityRepository.save(appointmentSlot);
            log.info("Appointment cancelled successfully");
        } catch (CustomException ex) {
            log.error("Error occurred while cancelling appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while cancelling appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while cancelling appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppointmentDto getAppointmentById(String appointmentId) throws CustomException {
        try{
            log.info("inside getAppointmentById method in DoctorAppointmentServiceImplementation");
            Optional<AppointmentEntity> appointmentOptional = doctorAppointmentRepository.findById(Long.parseLong(appointmentId));
            if (appointmentOptional.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with id: {}", appointmentId);
            AppointmentEntity appointment = appointmentOptional.get();
            return modelMapper.map(appointment, AppointmentDto.class);
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppointmentDto getAppointmentByAppointmentSlotId(String appointmentSlotId) throws CustomException {
        return null;
    }

    @Override
    public AppointmentDto getAppointmentByPatientId(String patientId) throws CustomException {
        try{
            log.info("inside getAppointmentByPatientId method in DoctorAppointmentServiceImplementation");
            Optional<AppointmentEntity> appointmentOptional = doctorAppointmentRepository.findByPatientId(patientId);
            if (appointmentOptional.isEmpty() || !appointmentOptional.get().getIsActive()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with patient id: {}", patientId);
            AppointmentEntity appointment = appointmentOptional.get();
            return modelMapper.map(appointment, AppointmentDto.class);
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AppointmentDto> getAppointmentByDoctorId(String doctorId) throws CustomException {
        try{
            log.info("inside getAppointmentByDoctorId method in DoctorAppointmentServiceImplementation");
            List<AppointmentEntity> appointments = doctorAppointmentRepository.findAllByDoctorId(doctorId);
            if (appointments.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with doctor id: {}", doctorId);

            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            for (AppointmentEntity appointment : appointments) {
                appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
            }
            return appointmentDtos;
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AppointmentDto> getAppointmentByDoctorIdAndDate(String doctorId, String date) throws CustomException {
        try{
            log.info("inside getAppointmentByDoctorIdAndDate method in DoctorAppointmentServiceImplementation");
            List<AppointmentEntity> appointments = doctorAppointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
            if (appointments.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with doctor id: {}", doctorId);

            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            for (AppointmentEntity appointment : appointments) {
                appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
            }
            return appointmentDtos;
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AppointmentDto> getBookedAppointmentByDoctorId(String doctorId) throws CustomException {
        try{
            log.info("inside getBookedAppointmentByDoctorId method in DoctorAppointmentServiceImplementation");
            List<AppointmentEntity> appointments = doctorAppointmentRepository.findAllByDoctorId(doctorId);
            if (appointments.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with doctor id: {}", doctorId);

            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            for (AppointmentEntity appointment : appointments) {
                if (appointment.getIsActive() && appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
                    appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
                }
            }
            return appointmentDtos;
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AppointmentDto> getBookedAppointmentByDoctorIdAndDate(String doctorId, String date) throws CustomException {
        try {
            log.info("inside getBookedAppointmentByDoctorIdAndDate method in DoctorAppointmentServiceImplementation");
            List<AppointmentEntity> appointments = doctorAppointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
            if (appointments.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Appointment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("Appointment found with doctor id: {}", doctorId);

            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            for (AppointmentEntity appointment : appointments) {
                if (appointment.getIsActive() && appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
                    appointmentDtos.add(modelMapper.map(appointment, AppointmentDto.class));
                }
            }
            return appointmentDtos;
        } catch (CustomException ex) {
            log.error("Error occurred while getting appointment: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while getting appointment: {}", ex.getMessage());
            throw new CustomException(new ResponseMessageDto("Unexpected error occurred while getting appointment", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isSlotOverlapping(AppointmentSlotRequestDto requestDto) throws CustomException {
        DoctorDto doctorDto = doctorService.getCurrentDoctor();
        List<DoctorAvailabilityEntity> existingSlots = doctorAvailabilityRepository.findByDoctorIdAndDate(
                doctorDto.getDoctorId(), requestDto.getDate());

        LocalTime newSlotStartTime = requestDto.getStartTime();
        LocalTime newSlotEndTime = requestDto.getEndTime();

        return existingSlots.stream().anyMatch(existingSlot -> {
            LocalTime existingSlotStartTime = existingSlot.getStartTime();
            LocalTime existingSlotEndTime = existingSlot.getEndTime();
            // if new slot starts or ends during an existing slot
            boolean overlaps = newSlotStartTime.isBefore(existingSlotEndTime) && newSlotEndTime.isAfter(existingSlotStartTime);
            // for exact match
            overlaps |= newSlotStartTime.equals(existingSlotStartTime) && newSlotEndTime.equals(existingSlotEndTime);

            return overlaps;
        });
    }



}
