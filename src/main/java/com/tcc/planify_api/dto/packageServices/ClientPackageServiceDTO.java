package com.tcc.planify_api.dto.packageServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPackageServiceDTO {
  private Long serviceId;
  private String serviceName;
  private Integer quantityLeft;
}