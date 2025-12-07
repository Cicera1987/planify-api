package com.tcc.planify_api.util;

import com.tcc.planify_api.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

  private AuthUtil() {}

  public static Long getAuthenticatedUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !(auth.getPrincipal() instanceof UserEntity user)) {
      throw new IllegalStateException("Usuário não autenticado ou inválido");
    }

    return user.getId();
  }
}