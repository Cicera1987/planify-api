package com.tcc.planify_api.dto.client;

import lombok.*;

@Data
public class ClientDTO extends ClientCreateDTO {
  private Long id;
  private Boolean active;
}
