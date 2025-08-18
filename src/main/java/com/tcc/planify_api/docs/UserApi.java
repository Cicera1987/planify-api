package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.Auth.UserLoginDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários")
public interface UserApi {

  @Operation(summary = "Listar usuários")
  @ApiResponse(responseCode = "200", description = "Lista retornada.")
  @GetMapping
  ResponseEntity<List<UserDTO>> getAllUsers();

  @Operation(summary = "Buscar usuário por ID")
  @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Encontrado."),
        @ApiResponse(responseCode = "404", description = "Não encontrado.")
  })
  @GetMapping("/{id}")
  ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id);
}
