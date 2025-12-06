package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.notification.NotificationTokenDTO;
import com.tcc.planify_api.entity.NotificationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notificações")
@RequestMapping("/notifications")
public interface NotificationApi {

  @Operation(summary = "Listar contatos que possuem notificações")
  @GetMapping("/contacts-with-notifications")
  ResponseEntity<List<Long>> getContactsWithNotifications();

  @Operation(summary = "Listar notificações do contato")
  @GetMapping("/list/{contactId}")
  ResponseEntity<List<NotificationEntity>> getNotifications(@PathVariable Long contactId);

  @Operation(summary = "Marcar notificação como lida")
  @PostMapping("/read/{notificationId}")
  ResponseEntity<Void> markAsRead(@PathVariable Long notificationId);

  @Operation(summary = "Salvar token FCM do contato")
  @PostMapping("/token")
  ResponseEntity<String> saveToken(
        @RequestBody NotificationTokenDTO dto
  );

  @Operation(
        summary = "Enviar notificação push",
        description = "Envia uma notificação via Firebase Cloud Messaging usando o token do dispositivo."
  )
  @ApiResponse(
        responseCode = "200",
        description = "Notificação enviada com sucesso."
  )
  @PostMapping("/send")
  ResponseEntity<String> sendNotification(
        @RequestParam String token,
        @RequestParam String title,
        @RequestParam String body
  );

  @Operation(summary = "Excluir UMA notificação")
  @DeleteMapping("/{notificationId}")
  ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId);

  @Operation(summary = "Excluir TODAS notificações do contato")
  @DeleteMapping("/contact/{contactId}")
  ResponseEntity<Void> deleteAllNotifications(@PathVariable Long contactId);
}