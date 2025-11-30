package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.NotificationApi;
import com.tcc.planify_api.dto.notification.NotificationTokenDTO;
import com.tcc.planify_api.entity.NotificationEntity;
import com.tcc.planify_api.service.NotificationHistoryService;
import com.tcc.planify_api.service.NotificationService;
import com.tcc.planify_api.service.NotificationTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController implements NotificationApi {

  private final NotificationService notificationService;
  private final NotificationTokenService tokenService;
  private final NotificationHistoryService historyService;

  @Override
  public ResponseEntity<List<Long>> getContactsWithNotifications() {
    return ResponseEntity.ok(historyService.getContactsWithNotifications());
  }

  @Override
  public ResponseEntity<List<NotificationEntity>> getNotifications(Long contactId) {
    return ResponseEntity.ok(historyService.getNotificationsByContact(contactId));
  }

  @Override
  public ResponseEntity<Void> markAsRead(Long notificationId) {
    historyService.markAsRead(notificationId);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<String> saveToken(@RequestBody NotificationTokenDTO dto) {
    tokenService.saveToken(dto);
    return ResponseEntity.ok("Token salvo com sucesso!");
  }

  @Override
  public ResponseEntity<String> sendNotification(String token, String title, String body) {
    notificationService.sendNotification(token, title, body);
    return ResponseEntity.ok("Notificação enviada!");
  }
}