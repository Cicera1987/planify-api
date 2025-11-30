package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.notification.NotificationTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notificações")
@RequestMapping("/notifications")
public interface NotificationApi {

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
}