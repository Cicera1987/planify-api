package com.tcc.planify_api.service;

import com.tcc.planify_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("Usuário Inválido!"));

    return new CustomUserPrincipal(
          user.getId(),
          user.getUsername(),
          user.getPassword(),
          user.getAuthorities()
    );
  }

  public void logout() {
    SecurityContextHolder.clearContext();
  }
}