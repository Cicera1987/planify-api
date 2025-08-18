package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.enums.PositionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
  Optional<PositionEntity> findByName(String name);
}

