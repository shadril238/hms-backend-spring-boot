package com.shadril.securityservice.repository;

import com.shadril.securityservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByEmail(String email);
    Optional<UserEntity> findUserById(Long id);
}
