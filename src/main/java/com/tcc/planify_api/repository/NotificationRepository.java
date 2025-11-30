package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findByContactIdOrderByCreatedAtDesc(Long contactId);
}