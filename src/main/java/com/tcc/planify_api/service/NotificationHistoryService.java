package com.tcc.planify_api.service;

import com.tcc.planify_api.entity.ContactEntity;
import com.tcc.planify_api.entity.NotificationEntity;
import com.tcc.planify_api.repository.ContactRepository;
import com.tcc.planify_api.repository.NotificationRepository;
import com.tcc.planify_api.repository.NotificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationHistoryService {

  private final NotificationRepository notificationRepository;
  private final ContactRepository contactRepository;

  public NotificationEntity saveNotification(Long contactId, String title, String message) {
    ContactEntity contact = contactRepository.findById(contactId)
          .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

    NotificationEntity notification = NotificationEntity.builder()
          .contact(contact)
          .title(title)
          .message(message)
          .read(false)
          .createdAt(LocalDateTime.now())
          .build();

    return notificationRepository.save(notification);
  }

  public List<NotificationEntity> getNotificationsByContact(Long contactId) {
    return notificationRepository.findByContact_IdOrderByCreatedAtDesc(contactId);
  }
  public List<Long> getContactsWithNotifications() {
    return notificationRepository.findDistinctContactIds();
  }
  public void markAsRead(Long notificationId) {
    NotificationEntity notification = notificationRepository.findById(notificationId)
          .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    notification.setRead(true);
    notificationRepository.save(notification);
  }
  public void deleteNotification(Long notificationId) {
    NotificationEntity notification = notificationRepository.findById(notificationId)
          .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

    notificationRepository.delete(notification);
  }

  public void deleteAllByContact(Long contactId) {
    List<NotificationEntity> list = notificationRepository.findByContact_IdOrderByCreatedAtDesc(contactId);
    notificationRepository.deleteAll(list);
  }
}