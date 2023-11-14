package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.DoctorAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, Long> {
    @Query("SELECT da FROM DoctorAvailabilityEntity da WHERE da.doctor.doctorId = :doctorId AND da.date = :date")
    List<DoctorAvailabilityEntity> findByDoctorIdAndDate(@Param("doctorId") String doctorId, @Param("date") LocalDate date);
}
