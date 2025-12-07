package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
  List<PackageEntity> findByOwnerId(Long ownerId);
  Optional<PackageEntity> findByIdAndOwnerId(Long id, Long ownerId);
}
