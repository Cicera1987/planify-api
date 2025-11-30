package com.tcc.planify_api.repository;

import com.tcc.planify_api.entity.NotificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationTokenRepository extends JpaRepository<NotificationTokenEntity, Long> {

  List<NotificationTokenEntity> findByContactId(Long contactId);
    NotificationTokenEntity findByToken(String token);
}