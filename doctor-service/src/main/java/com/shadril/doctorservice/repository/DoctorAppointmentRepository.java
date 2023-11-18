package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    Optional<AppointmentEntity> findByPatientId(String patientId);
    List<AppointmentEntity> findAllByDoctorId(String doctorId);
    List<AppointmentEntity> findAllByDoctorIdAndDate(String doctorId, String date);
}
