package com.tcc.planify_api.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  public void sendNotification(String token, String title, String body) {

    Notification notification = Notification.builder()
          .setTitle(title)
          .setBody(body)
          .build();

    Message message = Message.builder()
          .setToken(token)
          .setNotification(notification)
          .putData("route", "/scheduling")
          .build();

    try {
      String response = FirebaseMessaging.getInstance().send(message);
      System.out.println("Notificação enviada! ID: " + response);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao enviar notificação: " + e.getMessage(), e);
    }
  }
}