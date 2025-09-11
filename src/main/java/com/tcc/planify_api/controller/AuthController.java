package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.AuthApi;
import com.tcc.planify_api.dto.auth.UserLoginDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;

import com.tcc.planify_api.dto.user.UserUpdateDTO;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.security.TokenService;
import com.tcc.planify_api.service.AuthenticationService;
import com.tcc.planify_api.service.CloudinaryService;
import com.tcc.planify_api.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final TokenService tokenService;
  private final UserService userService;
  private final AuthenticationService authenticationService;

  @Override
  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
    UserDTO createdUser = userService.createUser(userCreateDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
  @Override
  public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
    UserDTO user = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
    UserEntity userEntity = userService.findByLogin(user.getUsername())
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    String token = tokenService.generateToken(userEntity);

    return ResponseEntity.ok(Map.of("token", token));
  }
  @Override
  public ResponseEntity<UserDTO> updateUser(Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
    UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
    return ResponseEntity.ok(updatedUser);
  }

  @Override
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    authenticationService.logout();
    Cookie cookie = new Cookie("JWT", null);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.noContent().build();
  }
}
