package com.shadril.doctorservice.entitiy;

import com.shadril.doctorservice.enums.AppointmentStatus;
import com.shadril.doctorservice.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Column(nullable = false)
    private String patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_id", nullable = false)
    private DoctorAvailabilityEntity doctorAvailability;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Column(nullable = false)
    private Boolean isActive = true;
}
