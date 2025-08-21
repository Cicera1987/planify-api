package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.TypeOfServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeOfServiceRepository extends JpaRepository<TypeOfServiceEntity, Long> {
  List<TypeOfServiceEntity> findByNameContainingIgnoreCase(String name);
}
