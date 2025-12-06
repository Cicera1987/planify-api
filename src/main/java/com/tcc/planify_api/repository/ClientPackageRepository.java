package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.ClientPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientPackageRepository extends JpaRepository<ClientPackageEntity, Long> {

  List<ClientPackageEntity> findByContactId(Long contactId);
}
