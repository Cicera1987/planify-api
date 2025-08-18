package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.AuthApi;
import com.tcc.planify_api.dto.Auth.UserLoginDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;

import com.tcc.planify_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
  private final UserService userService;

  @Override
  public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
    UserDTO createdUser = userService.createUser(userCreateDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
  @Override
  public ResponseEntity<UserDTO> login(@RequestBody UserLoginDTO loginDTO) {
    UserDTO user = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
    return ResponseEntity.ok(user);
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(Long id, UserCreateDTO userUpdateDTO) {
    UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
    return ResponseEntity.ok(updatedUser);
  }

}
