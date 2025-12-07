package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.Image.ImageSourceRequest;
import com.tcc.planify_api.repository.ImageProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageProviderService implements ImageProviderRepository {

  private final CloudinaryService cloudinaryService;

  @Override
  public String getImageUrl(ImageSourceRequest request) {

    if (request.isAmbiguous()) {
      throw new IllegalArgumentException("Envie apenas arquivo OU URL externa.");
    }

    if (!request.hasValidSource()) {
      throw new IllegalArgumentException("É necessário fornecer um arquivo ou uma URL.");
    }

    if (request.getFile() != null) {
      return cloudinaryService.uploadFile(request.getFile());
    }

    return request.getExternalUrl();
  }
}