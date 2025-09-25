package com.tcc.planify_api.dto.contact;

import com.tcc.planify_api.enums.Gender;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContactDTO extends ContactCreateDTO {
  private Long id;
  private String name;
  private String phone;
  private String email;
  private String observation;
  private String imageUrl;
  private Gender gender;
  private MultipartFile file;
  private Long professionalId;
  private List<Long> packageIds;
  private LocalDateTime createdAt;
}
