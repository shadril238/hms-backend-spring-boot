package com.shadril.patientservice.repository;

import com.shadril.patientservice.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    @Query("SELECT MAX(p.patientId) FROM PatientEntity p")
    Optional<String> findMaxPatientId();
}
