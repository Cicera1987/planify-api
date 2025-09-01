package com.tcc.planify_api.service;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserPrincipal {
  private Long id;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
}
