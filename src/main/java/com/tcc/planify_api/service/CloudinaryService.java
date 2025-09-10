package com.tcc.planify_api.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

  private final Cloudinary cloudinary;

  public String uploadFile(MultipartFile file) {
    try {
      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
      Object url = uploadResult.get("secure_url");
      if (url == null) {
        throw new RuntimeException("URL do Cloudinary é nula");
      }
      return url.toString();
    } catch (IOException e) {
      throw new RuntimeException("Falha ao enviar arquivo", e);
    }
  }
}