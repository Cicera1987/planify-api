package com.tcc.planify_api.dto.packageServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPackageDTO {
  private Long id;
  private String name;
  private List<ClientPackageServiceDTO> services;
}
