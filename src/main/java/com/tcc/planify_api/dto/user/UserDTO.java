package com.tcc.planify_api.dto.user;
import com.tcc.planify_api.enums.PositionEnum;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
  private Long id;
  private String username;
  private String email;
  private String phone;
  private String speciality;
  private PositionEnum position;
  private String imageUrl;
  private MultipartFile file;
  private boolean active;
}

