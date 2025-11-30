package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

  @Query("SELECT DISTINCT notification.contact.id FROM NotificationEntity notification")
  List<Long> findDistinctContactIds();

  List<NotificationEntity> findByContact_IdOrderByCreatedAtDesc(Long contactId);
}