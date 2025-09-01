package com.tcc.planify_api.security;

import com.tcc.planify_api.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
  private static final String TOKEN_PREFIX = "Bearer";
  private static final String POSITION_CLAIM = "position";

  @Value("${jwt.expiration}")
  private String expiration;

  @Value("${jwt.secret}")
  private String secret;

  public String generateToken(UserEntity userEntity) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + Long.parseLong(expiration));

    List<String> roles = List.of("ROLE_" + userEntity.getPosition().getAuthority());

    return TOKEN_PREFIX + " " +
          Jwts.builder()
                .setIssuer("planify-api")
                .claim(Claims.ID, userEntity.getId().toString())
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
    if (token != null) {
      Claims body = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token.replace(TOKEN_PREFIX, "").trim())
            .getBody();

      String userId = body.get(Claims.ID, String.class);
      List<String> roles = body.get(POSITION_CLAIM, List.class);

      if (userId != null && roles != null) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
      }
    }
    return null;
  }
}
