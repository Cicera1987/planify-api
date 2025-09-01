package com.tcc.planify_api.security;

import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.service.CustomUserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

  private static final String TOKEN_PREFIX = "Bearer";
  private static final String POSITION_CLAIM = "position";

  private final UserRepository userRepository;

  @Value("${jwt.expiration}")
  private String expiration;

  @Value("${jwt.secret}")
  private String secret;

  public String generateToken(UserEntity userEntity) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + Long.parseLong(expiration));

    List<String> roles = List.of(userEntity.getPosition().getAuthority());

    return TOKEN_PREFIX + " " +
          Jwts.builder()
                .setIssuer("planify-api")
                .setSubject(userEntity.getUsername())
                .claim(Claims.ID, userEntity.getId().toString()) // salvar como string
                .claim(POSITION_CLAIM, roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
  }

  public Long getUserIdFromToken(String token) {
    if (token == null || token.isBlank()) return null;

    Claims body = Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
          .build()
          .parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
          .getBody();

    String userId = body.get(Claims.ID, String.class);
    return userId != null ? Long.parseLong(userId) : null;
  }

  public UsernamePasswordAuthenticationToken isValid(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
            .getBody();

      String username = claims.getSubject();
      Long userId = Long.parseLong(claims.get(Claims.ID, String.class));

      List<String> roles = claims.get(POSITION_CLAIM, List.class);
      List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());

      CustomUserPrincipal principal = new CustomUserPrincipal(userId, username, null, authorities);

      return new UsernamePasswordAuthenticationToken(principal, null, authorities);

    } catch (JwtException e) {
      return null;
    }
  }
}
