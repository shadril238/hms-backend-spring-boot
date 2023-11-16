package com.shadril.notificationservice.repository;

import com.shadril.notificationservice.entity.NotificationPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreferenceEntity, Long> {
    List<NotificationPreferenceEntity> findAllByUserId(Long userId);

    @Query("SELECT n FROM NotificationPreferenceEntity n WHERE n.userId = :userId AND n.prefType = :prefType")
    Optional<NotificationPreferenceEntity> findByUserIdAndPrefType(@Param("userId") Long userId, @Param("prefType") String prefType);
}
