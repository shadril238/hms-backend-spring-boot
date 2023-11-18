package com.shadril.pharmaceuticalinventoryservice.repository;

import com.shadril.pharmaceuticalinventoryservice.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {
}
