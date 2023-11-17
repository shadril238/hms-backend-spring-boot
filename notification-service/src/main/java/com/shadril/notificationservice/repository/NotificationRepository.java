package com.shadril.notificationservice.repository;

import com.shadril.notificationservice.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserId(Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.userId = :userId AND n.isRead = false")
    List<NotificationEntity> findUnreadByUserId(@Param("userId") Long userId);
}
