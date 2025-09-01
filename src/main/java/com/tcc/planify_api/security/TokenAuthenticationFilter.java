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

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final String BEARER = "Bearer";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String tokenFromHeader = getTokenFromHeader(request);

    if (tokenFromHeader != null && !tokenFromHeader.isBlank()) {
      UsernamePasswordAuthenticationToken auth = tokenService.isValid(tokenFromHeader);
      if (auth != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromHeader(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    return token != null ? token.replace(BEARER, "").trim() : null;
  }
}