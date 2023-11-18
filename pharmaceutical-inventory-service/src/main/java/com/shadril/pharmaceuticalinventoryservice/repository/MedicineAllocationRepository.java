package com.shadril.pharmaceuticalinventoryservice.repository;

import com.shadril.pharmaceuticalinventoryservice.entity.MedicineAllocationEntity;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineAllocationRepository extends JpaRepository<MedicineAllocationEntity, Long> {
    @Query("SELECT m FROM MedicineAllocationEntity m WHERE m.medicine = :medicine AND m.patientId = :patientId")
    Optional<MedicineAllocationEntity> findByMedicineAndPatientId(@Param("medicine") MedicineEntity medicine, @Param("patientId") String patientId);
}
