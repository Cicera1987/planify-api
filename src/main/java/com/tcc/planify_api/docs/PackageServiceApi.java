package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.packageServices.PackageCreateDTO;
import com.tcc.planify_api.dto.packageServices.PackageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pacotes")
@RequestMapping("/packages")
public interface PackageServiceApi {

  @Operation(summary = "Listar todos os pacotes de um proprietário")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacotes retornada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping
  ResponseEntity<List<PackageDTO>> getAllPackages();

  @Operation(summary = "Criar novo pacote")
  @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pacote criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping
  ResponseEntity<PackageDTO> createPackage(@Valid @RequestBody PackageCreateDTO packageCreateDTO);

  @Operation(summary = "Atualizar pacote existente")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pacote atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "404", description = "Pacote não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping("/{id}")
  ResponseEntity<PackageDTO> updatePackage(@PathVariable("id") Long id,
                                           @Valid @RequestBody PackageCreateDTO packageUpdateDTO);

  @Operation(summary = "Deletar pacote")
  @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pacote excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Pacote não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deletePackage(@PathVariable("id") Long id);
}
