package com.tcc.planify_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final String BEARER = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
        throws ServletException, IOException {

    String token = getTokenFromHeader(request);

    if (token != null && !token.isBlank()) {
      Long userId = tokenService.getUserIdFromToken(token);
      if (userId != null) {
        UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userId, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromHeader(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    return token != null ? token.replace(BEARER, "").trim() : null;
  }
}