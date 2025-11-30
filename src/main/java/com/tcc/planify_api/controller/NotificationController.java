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
  private final NotificationHistoryService notificationHistoryService;

  @Override
  public ResponseEntity<String> saveToken(@RequestBody NotificationTokenDTO dto) {
    tokenService.saveToken(dto);
    return ResponseEntity.ok("Token salvo com sucesso!");
  }

  @Override
  public ResponseEntity<String> sendNotification(
        @RequestParam String token,
        @RequestParam String title,
        @RequestParam String body
  ) {
    notificationService.sendNotification(token, title, body);
    return ResponseEntity.ok("Notificação enviada!");
  }

  @Override
  public ResponseEntity<List<NotificationEntity>> getNotifications(@PathVariable Long contactId) {
    List<NotificationEntity> notifications = notificationHistoryService.getNotificationsByContact(contactId);
    return ResponseEntity.ok(notifications);
  }

  @Override
  public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
    notificationHistoryService.markAsRead(notificationId);
    return ResponseEntity.ok().build();
  }
}