package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.ClientEntity;
import com.tcc.planify_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}
