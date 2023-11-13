package com.shadril.patientservice.repository;

import com.shadril.patientservice.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    @Query("SELECT MAX(p.patientId) FROM PatientEntity p")
    Optional<String> findMaxPatientId();

    Optional<PatientEntity> findByEmail(String email);
}
