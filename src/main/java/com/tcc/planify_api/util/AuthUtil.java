package com.tcc.planify_api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

  private AuthUtil() {}

  public static Long getAuthenticatedProfessionalId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      throw new IllegalStateException("Usuário não autenticado");
    }

    try {
      return Long.parseLong(auth.getPrincipal().toString());
    } catch (NumberFormatException e) {
      throw new IllegalStateException("ID do usuário inválido no contexto de autenticação");
    }
  }
}
