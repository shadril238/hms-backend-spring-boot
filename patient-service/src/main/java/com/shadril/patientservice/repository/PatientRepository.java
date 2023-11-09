package com.shadril.patientservice.repository;

import com.shadril.patientservice.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, String> {

}
