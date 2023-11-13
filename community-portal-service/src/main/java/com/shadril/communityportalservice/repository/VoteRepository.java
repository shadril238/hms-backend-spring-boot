package com.shadril.communityportalservice.repository;

import com.shadril.communityportalservice.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    @Query("SELECT v FROM VoteEntity v WHERE v.patientId = :patientId AND v.post.postId = :postId")
    Optional<VoteEntity> findByPatientIdAndPostId(@Param("patientId") String patientId, @Param("postId") Long postId);
}
