package com.tcc.planify_api.dto.user;

import com.tcc.planify_api.enums.PositionEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {

  @NotEmpty
  private String username;

  @Email
  @NotEmpty
  private String email;

  @NotEmpty
  private String phone;

  @NotEmpty
  private String password;

  @NotEmpty
  private String speciality;

  @Builder.Default
  private PositionEnum position = PositionEnum.PROFESSIONAL;

  private MultipartFile file;

  private String imageUrl;

  private Boolean active;
}