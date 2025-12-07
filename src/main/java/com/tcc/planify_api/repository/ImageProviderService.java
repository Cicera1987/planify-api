package com.tcc.planify_api.repository;

import com.tcc.planify_api.dto.Image.ImageSourceRequest;

public interface ImageProviderService {
  String getImageUrl(ImageSourceRequest request);
}
