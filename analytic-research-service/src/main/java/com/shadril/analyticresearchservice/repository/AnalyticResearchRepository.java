package com.shadril.analyticresearchservice.repository;

import com.shadril.analyticresearchservice.entity.ResearcherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticResearchRepository extends JpaRepository<ResearcherEntity, Long> {
    Optional<ResearcherEntity> findByEmail(String email);
    List<ResearcherEntity> findAllByIsValid(Boolean isValid);
    List<ResearcherEntity> findAllByIsTaken(Boolean isTaken);
    ResearcherEntity findByIdAndIsValidIsFalse(Long id);
}
