package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários")
public interface UserApi {

  @Operation(summary = "Listar usuários com paginação")
  @ApiResponse(responseCode = "200", description = "Lista paginada de usuários retornada.")
  @GetMapping
  ResponseEntity<PageDTO<UserDTO>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "Listar usuários com paginação e filtros")
  @ApiResponse(responseCode = "200", description = "Lista paginada de usuários filtrada retornada.")
  @GetMapping("/search")
  ResponseEntity<PageDTO<UserDTO>> searchUsers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String speciality,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "Buscar usuário por ID")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Encontrado."),
        @ApiResponse(responseCode = "404", description = "Não encontrado.")
  })
  @GetMapping("/{id}")
  ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id);
}
