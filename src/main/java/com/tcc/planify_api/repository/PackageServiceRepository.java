package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.PackageServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageServiceEntity, Long> {
  List<PackageServiceEntity> findByPackageEntityId(Long packageId);
}
