package com.shadril.doctorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentAllDataDto {
    private DoctorAvailabilityDto doctorAvailability;
    private AppointmentDto appointment;
}
