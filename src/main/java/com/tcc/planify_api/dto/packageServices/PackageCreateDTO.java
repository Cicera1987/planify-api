package com.tcc.planify_api.dto.packageServices;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageCreateDTO {

  @NotNull(message = "O nome do pacote é obrigatório")
  @Size(min = 2, max = 255, message = "O nome do pacote deve ter entre 2 e 255 caracteres")
  private String name;

  @NotNull(message = "O preço total é obrigatório")
  private Double totalPrice;

  @NotNull(message = "O número de sessões é obrigatório")
  private Integer numberSessions;

  @NotNull(message = "Deve haver pelo menos um serviço no pacote")
  @Size(min = 1, message = "O pacote deve conter pelo menos um serviço")
  private List<Long> serviceIds;
}
