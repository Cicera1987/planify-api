package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.UploadImagApi;
import com.tcc.planify_api.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController  implements UploadImagApi {
  private  final CloudinaryService  cloudinaryService;

  @Override
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("Arquivo vazio");
    }
    String imageUrl = cloudinaryService.uploadFile(file);
    return ResponseEntity.ok(imageUrl);
  }
}
