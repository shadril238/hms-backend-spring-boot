package com.shadril.pharmaceuticalinventoryservice.repository;

import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentAllocationEntity;
import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalEquipmentAllocationRepository extends JpaRepository<MedicalEquipmentAllocationEntity, Long> {
    @Query("SELECT m FROM MedicalEquipmentAllocationEntity m WHERE m.medicalEquipment = :medicalEquipment AND m.patientId = :patientId")
    Optional<MedicalEquipmentAllocationEntity> findByMedicalEquipmentAndPatientId(@Param("medicalEquipment") MedicalEquipmentEntity medicalEquipment, @Param("patientId") String patientId);
}
