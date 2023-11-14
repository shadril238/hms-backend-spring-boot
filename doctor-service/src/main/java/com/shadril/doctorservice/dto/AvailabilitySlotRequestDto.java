package com.shadril.doctorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlotRequestDto {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration perPatientTime;
}
