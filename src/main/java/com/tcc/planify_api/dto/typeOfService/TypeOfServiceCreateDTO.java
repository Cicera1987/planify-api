package com.tcc.planify_api.dto.typeOfService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfServiceCreateDTO {
  @NotNull
  @Size(min = 1, max = 255)
  private String name;

  private String description;

  @NotNull
  private BigDecimal price;

  @Size(max = 100)
  private String category;

  @NotNull
  private Integer duration;
}
