package com.shadril.pharmaceuticalinventoryservice.repository;

import com.shadril.pharmaceuticalinventoryservice.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {
    @Query("SELECT m FROM MedicineEntity m WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MedicineEntity> findByMedicineNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM MedicineEntity m WHERE LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :manufacturer, '%'))")
    List<MedicineEntity> findByManufacturerContainingIgnoreCase(String manufacturer);

    @Query("SELECT m FROM MedicineEntity m WHERE LOWER(m.genericName) LIKE LOWER(CONCAT('%', :genericName, '%'))")
    List<MedicineEntity> findByGenericNameContainingIgnoreCase(String genericName);
}
