package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String userName);
  Optional<UserEntity> findByEmail(String email);

  boolean existsByUsername(String userName);
}
