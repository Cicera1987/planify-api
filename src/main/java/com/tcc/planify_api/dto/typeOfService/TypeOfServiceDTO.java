package com.tcc.planify_api.dto.typeOfService;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TypeOfServiceDTO {
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private String category;
  private Integer duration;
  private Long ownerId;
  private Integer quantity;
  private LocalDateTime createdAt;
}
