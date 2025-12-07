package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
  Page<ContactEntity> findByProfessionalIdAndNameContainingIgnoreCase(Long professionalId, String name, Pageable pageable);

}

