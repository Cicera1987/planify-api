package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.AuthApi;
import com.tcc.planify_api.dto.Auth.UserLoginDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;

import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.security.TokenService;
import com.tcc.planify_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final UserService userService;

  @Override
  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
    UserDTO createdUser = userService.createUser(userCreateDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
    UserDTO user = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
    UserEntity userEntity = userService.findByLogin(user.getUsername())
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    String token = tokenService.generateToken(userEntity);

    return ResponseEntity.ok(token);
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(Long id, @Valid @RequestBody UserCreateDTO userUpdateDTO) {
    UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
    return ResponseEntity.ok(updatedUser);
  }
}
