package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.contact.ContactCreateDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.dto.pagination.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Contato")
public interface ContactApi {

  @Operation(summary = "Listar todos os contatos", description = "Retorna uma lista paginada de contatos cadastrados pelo profissional.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso. Retorna a lista de contatos."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping
  ResponseEntity<PageDTO<ContactDTO>> listContact(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "Buscar contatos por nome", description = "Retorna uma lista paginada de contatos filtrados pelo nome do contato do profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping("/search")
  ResponseEntity<PageDTO<ContactDTO>> searchContacts(
        @RequestParam String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "Criar um novo contato", description = "Cria um novo contato para o profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contato criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Os dados fornecidos não são válidos."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<ContactDTO> createContact(@Valid @ModelAttribute ContactCreateDTO contactCreateDTO);

  @Operation(summary = "Atualizar um contato existente", description = "Atualiza os dados de um contato existente com o ID fornecido.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. O ID ou os dados fornecidos não são válidos."),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping(value = "/{idContato}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<ContactDTO> updateContact(
        @NotNull @PathVariable("idContato") Long idContato,
        @Valid @ModelAttribute ContactCreateDTO contactCreateDTO) throws Exception;

  @Operation(summary = "Deletar um contato", description = "Deleta o contato com o ID fornecido.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contato excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{idContato}")
  ResponseEntity<Void> deleteContact(@NotNull @PathVariable("idContato") Long idContato) throws Exception;
}