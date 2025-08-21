package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.typeOfService.TypeOfServiceCreateDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Serviços")
@RequestMapping("/services")
public interface TypeOfServiceApi {

  @Operation(summary = "Listar todos os serviços")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping
  ResponseEntity<List<TypeOfServiceDTO>> getAllServices();

  @Operation(summary = "Buscar serviço por ID")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping("/{id}")
  ResponseEntity<TypeOfServiceDTO> getServiceById(@PathVariable("id") Long id);

  @Operation(summary = "Criar novo serviço")
  @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping
  ResponseEntity<TypeOfServiceDTO> createService(@Valid @RequestBody TypeOfServiceCreateDTO serviceCreateDTO);

  @Operation(summary = "Atualizar serviço existente")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping("/{id}")
  ResponseEntity<TypeOfServiceDTO> updateService(@PathVariable("id") Long id,
                                                 @Valid @RequestBody TypeOfServiceCreateDTO serviceUpdateDTO);

  @Operation(summary = "Deletar serviço")
  @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Serviço excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteService(@PathVariable("id") Long id);

  @Operation(summary = "Buscar serviços por nome")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping("/search")
  ResponseEntity<List<TypeOfServiceDTO>> searchByName(@RequestParam("name") String name);
}
