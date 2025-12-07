package com.tcc.planify_api.dto.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSourceRequest {
  private MultipartFile file;
  private String externalUrl;
  private String provider;
  private String providerUserId;
}
