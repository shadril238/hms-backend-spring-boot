package com.shadril.patientservice.repository;

import com.shadril.patientservice.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    @Query("SELECT MAX(p.patientId) FROM PatientEntity p")
    Optional<String> findMaxPatientId();

    Optional<PatientEntity> findByEmail(String email);

    @Query("SELECT p FROM PatientEntity p WHERE p.isApproved = :approved")
    List<PatientEntity> findAllByApproved(@Param("approved") Boolean approved);

    @Query("SELECT p FROM PatientEntity p WHERE p.userId = :userId")
    Optional<PatientEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM PatientEntity p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PatientEntity> searchByFirstNameOrLastNameContainingIgnoreCase(String name);
}
