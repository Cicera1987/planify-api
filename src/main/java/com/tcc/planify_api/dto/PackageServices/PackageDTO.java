package com.tcc.planify_api.dto.PackageServices;

import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO {

  private Long id;
  private String name;
  private Double totalPrice;
  private Integer numberSessions;
  private Long ownerId;
  private LocalDateTime createdAt;

  private List<TypeOfServiceDTO> services;
}

