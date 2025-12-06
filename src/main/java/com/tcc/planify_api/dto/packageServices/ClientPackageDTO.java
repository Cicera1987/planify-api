package com.tcc.planify_api.dto.packageServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPackageDTO {
  private Long id; // id do ClientPackageEntity
  private String name; // nome do pacote
  private List<ClientPackageServiceDTO> services; // quantidade restante por serviço
}
