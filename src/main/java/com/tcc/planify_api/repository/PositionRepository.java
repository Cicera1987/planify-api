package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.enums.PositionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
  @Query("SELECT p FROM position p WHERE p.position = :position")
  Optional<PositionEntity> findByPosition(@Param("position") PositionEnum position);
}

