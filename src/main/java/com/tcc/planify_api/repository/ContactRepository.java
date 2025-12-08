package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
  Page<ContactEntity> findByProfessionalIdAndNameContainingIgnoreCase(Long professionalId, String name, Pageable pageable);
  Page<ContactEntity> findByProfessionalId(Long professionalId, Pageable pageable);
  Optional<ContactEntity> findByIdAndProfessionalId(Long id, Long professionalId);
  void deleteByIdAndProfessionalId(Long id, Long professionalId);
}
