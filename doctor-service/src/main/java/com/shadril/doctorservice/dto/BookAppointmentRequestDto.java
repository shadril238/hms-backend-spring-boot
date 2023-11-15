package com.shadril.doctorservice.dto;

import com.shadril.doctorservice.enums.AppointmentStatus;
import com.shadril.doctorservice.enums.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAppointmentRequestDto {
    private Long availabilityId;
    private String patientId;
    private AppointmentType appointmentType;
    private AppointmentStatus appointmentStatus;
}
