package com.tcc.planify_api.dto.contact;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class ContactDTO extends ContactCreateDTO {
  private Long id;
  private String name;
  private String phone;
  private String email;
  private String observation;

  private Long professionalId;
  private LocalDateTime createdAt;
}
