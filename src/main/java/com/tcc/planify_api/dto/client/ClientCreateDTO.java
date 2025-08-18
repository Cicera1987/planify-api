package com.tcc.planify_api.dto.client;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDTO {

  @NotEmpty
  private String name;

  @Email
  @NotEmpty
  private String email;

  @NotEmpty
  private String phone;

  @NotEmpty
  private String password;

  @NotEmpty
  private String speciality;
}
