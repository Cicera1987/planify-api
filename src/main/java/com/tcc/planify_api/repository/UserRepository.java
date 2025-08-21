package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  @Query("SELECT u FROM UserEntity u " +
        "WHERE (:name IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (:speciality IS NULL OR LOWER(u.speciality) LIKE LOWER(CONCAT('%', :speciality, '%')))")
  Page<UserEntity> searchUsers(@Param("name") String name,
                               @Param("speciality") String speciality,
                               Pageable pageable);
  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findByUsername(String username);
}
