package com.shadril.doctorservice.service.implementation;

import com.shadril.doctorservice.dto.AvailabilitySlotRequestDto;
import com.shadril.doctorservice.dto.DoctorDto;
import com.shadril.doctorservice.dto.ResponseMessageDto;
import com.shadril.doctorservice.entitiy.DoctorAvailabilityEntity;
import com.shadril.doctorservice.entitiy.DoctorEntity;
import com.shadril.doctorservice.exception.CustomException;
import com.shadril.doctorservice.repository.DoctorAvailabilityRepository;
import com.shadril.doctorservice.service.DoctorAvailabilityService;
import com.shadril.doctorservice.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DoctorAvailabilityServiceImplementation implements DoctorAvailabilityService {
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @Override
    public void createAvailabilitySlots(AvailabilitySlotRequestDto requestDto)
            throws CustomException {
        try {
            log.info("inside createAvailabilitySlots method in DoctorAvailabilityServiceImplementation");
            DoctorDto doctorDto = doctorService.getCurrentDoctor();
            log.info("Doctor found with id: {}", doctorDto.getDoctorId());

            DoctorEntity doctorEntity = modelMapper.map(doctorDto, DoctorEntity.class);
            List<DoctorAvailabilityEntity> availabilitySlots = new ArrayList<>();
            LocalTime slotTime = requestDto.getStartTime();
            Long perPatientTime = requestDto.getPerPatientTimeInMinutes();

            while (slotTime.isBefore(requestDto.getEndTime())) {
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

}
