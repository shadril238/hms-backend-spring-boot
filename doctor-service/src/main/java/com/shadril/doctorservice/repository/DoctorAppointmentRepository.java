package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    @Query("SELECT a FROM AppointmentEntity a WHERE a.patientId = :patientId")
    Optional<AppointmentEntity> findByPatientId(@Param("patientId") String patientId);
    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.doctorId = :doctorId")
    List<AppointmentEntity> findAllByDoctorId(@Param("doctorId") String doctorId);
    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.doctorId = :doctorId AND a.doctorAvailability.date = :date")
    List<AppointmentEntity> findAllByDoctorIdAndDate(@Param("doctorId") String doctorId, @Param("date") LocalDate date);
    @Query("SELECT COUNT(a) > 0 FROM AppointmentEntity a " +
            "JOIN a.doctor d " +
            "JOIN a.doctorAvailability da " +
            "WHERE a.patientId = :patientId " +
            "AND d.doctorId = :doctorId " +
            "AND da.date = :date " +
            "AND a.appointmentStatus = com.shadril.doctorservice.enums.AppointmentStatus.Booked")
    boolean existsByPatientIdAndDoctorAndDate(@Param("patientId") String patientId,
                                              @Param("doctorId") String doctorId,
                                              @Param("date") LocalDate date);

}
