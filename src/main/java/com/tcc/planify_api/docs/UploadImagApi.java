package com.tcc.planify_api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Upload de Imagem")
public interface UploadImagApi {

  @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem e retorna a URL.")
  @ApiResponse(responseCode = "200", description = "Imagem enviada com sucesso.")
  @PostMapping
  ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file);
}
