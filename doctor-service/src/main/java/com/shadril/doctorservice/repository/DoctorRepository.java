package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, String> {
    @Query("SELECT MAX(d.doctorId) FROM DoctorEntity d")
    Optional<String> findMaxDoctorId();

    @Query("SELECT d FROM DoctorEntity d WHERE d.email = :email")
    Optional<DoctorEntity> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT d.department FROM DoctorEntity d WHERE d.department IS NOT NULL AND d.department <> '' AND d.isApproved = true")
    List<String> findDistinctDepartment();

    @Query("SELECT d FROM DoctorEntity d WHERE LOWER(d.department) LIKE LOWER(CONCAT('%', :department, '%')) AND d.isApproved = true")
    List<DoctorEntity> findByDepartment(String department);


    @Query("SELECT d FROM DoctorEntity d WHERE d.isApproved = true")
    List<DoctorEntity> findAllApprovedDoctor();

    @Query("SELECT d FROM DoctorEntity d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<DoctorEntity> searchByFirstNameOrLastNameContainingIgnoreCase(String name);
}