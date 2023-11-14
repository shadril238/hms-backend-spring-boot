package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
}
