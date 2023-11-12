package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, String> {
    @Query("SELECT MAX(d.doctorId) FROM DoctorEntity d")
    Optional<String> findMaxDoctorId();
}