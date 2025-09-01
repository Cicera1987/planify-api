package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.UserApi;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<PageDTO<UserDTO>> getAllUsers(int page, int size) {
    return ResponseEntity.ok(userService.getAllUsers(page, size));
  }

  @Override
  public ResponseEntity<PageDTO<UserDTO>> searchUsers(String name, String speciality, int page, int size) {
    return ResponseEntity.ok(userService.getSearchUsers(name, speciality, page, size));
  }

  @Override
  public ResponseEntity<UserDTO> getUserById(Long id) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId;
    if (principal instanceof String) {
      userId = Long.parseLong((String) principal);
    } else if (principal instanceof Long) {
      userId = (Long) principal;
    } else {
      throw new IllegalStateException("Principal inválido no SecurityContext");
    }

    UserDTO user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

}
