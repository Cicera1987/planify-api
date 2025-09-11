package com.tcc.planify_api.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreateDTO {
  @NotBlank(message = "O nome do cliente é obrigatório")
  private String name;

  @NotBlank(message = "O telefone do cliente é obrigatório")
  private String phone;

  @Email(message = "Email inválido")
  private String email;

  private String imageUrl;
  private MultipartFile file;

  private List<Long> packageIds;

  private String observation;
}
