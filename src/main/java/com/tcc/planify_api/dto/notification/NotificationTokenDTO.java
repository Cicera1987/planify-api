package com.tcc.planify_api.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTokenDTO {
  private Long contactId;
  private String token;
  private String deviceInfo;
}
