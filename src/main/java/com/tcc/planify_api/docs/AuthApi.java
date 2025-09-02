package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.auth.UserLoginDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.dto.user.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth")
public interface AuthApi {

  @Operation(summary = "Registrar novo usuário",
        description = "Cria um novo usuário. Padrão de posição = PROFESSIONAL, salvo se informado ADMIN/CLIENT.")
  @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.")
  @PostMapping("/register")
  ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO);

  @Operation(summary = "Login", description = "Autentica por e-mail e senha.")
  @ApiResponse(responseCode = "200", description = "Login feito com sucesso.")
  @PostMapping("/login")
  ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO loginDTO);

  @Operation(summary = "Atualizar usuário",
        description = "Atualiza os dados do usuário com base no ID fornecido.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
        @ApiResponse(responseCode = "403", description = "Acesso negado. Entre em contato com o suporte para mais informações."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.")
  })
  @PatchMapping("/{id}")
  ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO);

  @Operation(summary = "Logout", description = "Encerra a sessão do usuário atual.")
  @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso.")
  @PostMapping("/logout")
  ResponseEntity<Void> logout(HttpServletResponse response);

}