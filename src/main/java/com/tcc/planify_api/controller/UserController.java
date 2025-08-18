package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.UserApi;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Override
  public ResponseEntity<UserDTO> getUserById(Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }
}
