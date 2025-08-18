package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.client.ClientCreateDTO;
import com.tcc.planify_api.dto.client.ClientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profissional")
public interface ClientApi {

  @Operation(summary = "Listar todos os profissionais", description = "Retorna uma lista de todos os profissionais cadastrados no sistema.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso. Retorna a lista de profissionais."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping
  ResponseEntity<List<ClientDTO>> listarProfissionais();

  @Operation(summary = "Criar um novo profissional", description = "Cria um novo profissional com os dados fornecidos.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Os dados fornecidos não são válidos."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping
  ResponseEntity<ClientDTO> criarProfissional(@Valid @RequestBody ClientCreateDTO profissionalCreateDTO);

  @Operation(summary = "Atualizar um profissional existente", description = "Atualiza os dados de um profissional existente com o ID fornecido.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profissional atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. O ID ou os dados fornecidos não são válidos."),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping("/{idProfissional}")
  ResponseEntity<ClientDTO> atualizarProfissional(
        @NotNull @PathVariable("idProfissional") Long idProfissional,
        @Valid @RequestBody ClientCreateDTO profissionalCreateDTO) throws Exception;

  @Operation(summary = "Deletar um profissional", description = "Deleta o profissional com o ID fornecido.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profissional excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Profissional não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{idProfissional}")
  ResponseEntity<Void> deletarProfissional(@NotNull @PathVariable("idProfissional") Long idProfissional) throws Exception;
}
