package com.shadril.pharmaceuticalinventoryservice.repository;

import com.shadril.pharmaceuticalinventoryservice.entity.MedicalEquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalEquipmentRepository extends JpaRepository<MedicalEquipmentEntity, Long> {
    @Query("SELECT me FROM MedicalEquipmentEntity me WHERE me.medicalEquipmentName = :name")
    List<MedicalEquipmentEntity> findByMedicalEquipmentName(String name);
    @Query("SELECT me FROM MedicalEquipmentEntity me WHERE me.manufacturer = :manufacturer")
    List<MedicalEquipmentEntity> findByManufacturer(String manufacturer);
}
