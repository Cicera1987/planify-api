package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.ClientPackageServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPackageServiceRepository extends JpaRepository<ClientPackageServiceEntity, Long> {}