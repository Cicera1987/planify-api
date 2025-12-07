package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.TypeOfServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeOfServiceRepository extends JpaRepository<TypeOfServiceEntity, Long> {

  List<TypeOfServiceEntity> findByOwnerId(Long ownerId);

  Optional<TypeOfServiceEntity> findByIdAndOwnerId(Long id, Long ownerId);

  List<TypeOfServiceEntity> findByOwnerIdAndNameContainingIgnoreCase(Long ownerId, String name);

}
