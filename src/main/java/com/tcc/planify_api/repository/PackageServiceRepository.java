package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.PackageServiceEntity;
import com.tcc.planify_api.entity.SchedulingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageServiceEntity, Long> {
  List<PackageServiceEntity> findByPackageEntityId(Long packageId);
  Optional<PackageServiceEntity> findByPackageEntityIdAndServiceId(Long packageId, Long serviceId);
}
