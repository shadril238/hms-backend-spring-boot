package com.shadril.doctorservice.repository;

import com.shadril.doctorservice.entitiy.RoomEntity;
import jakarta.ws.rs.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT r FROM RoomEntity r WHERE r.roomNo = :roomNo AND r.isAvailable = true")
    Optional<RoomEntity> isRoomAvailable(@Param("roomNo") String roomNo);
}
