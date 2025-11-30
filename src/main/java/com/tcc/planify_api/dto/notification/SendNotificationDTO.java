package com.tcc.planify_api.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNotificationDTO {
  private String token;
  private String title;
  private String body;
}