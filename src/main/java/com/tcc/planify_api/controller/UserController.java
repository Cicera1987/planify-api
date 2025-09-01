package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.UserApi;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.service.CustomUserPrincipal;
import com.tcc.planify_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
  public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
    CustomUserPrincipal principal = Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
          .filter(p -> p instanceof CustomUserPrincipal)
          .map(p -> (CustomUserPrincipal) p)
          .orElseThrow(() -> new IllegalStateException("Principal inválido"));

    boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && !principal.getId().equals(id)) {
      return ResponseEntity.status(403).build();
    }

    UserDTO user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

}
