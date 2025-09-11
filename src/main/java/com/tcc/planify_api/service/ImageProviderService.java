package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.Image.ImageSourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageProviderService implements com.tcc.planify_api.repository.ImageProviderService {

  private final CloudinaryService cloudinaryService;

  @Override
  public String getImageUrl(ImageSourceRequest request) {
    if (request.getFile() != null) {
      return cloudinaryService.uploadFile(request.getFile());
    }
    if (request.getExternalUrl() != null) {
      return request.getExternalUrl();
    }
    throw new IllegalArgumentException("Não foi possível determinar a origem da imagem.");
  }
}