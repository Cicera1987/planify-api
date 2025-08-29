package com.tcc.planify_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final TokenService tokenService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
          .headers(headers -> headers.frameOptions(frame -> frame.disable()))
          .cors(cors -> {})
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/clients/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .requestMatchers("/contacts/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .requestMatchers("/services/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .requestMatchers("/packages/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .requestMatchers("/calendar/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .requestMatchers("/scheduling/**").hasAnyRole("ADMIN", "PROFESSIONAL")
                .anyRequest().hasRole("ADMIN")
          );

    http.addFilterBefore(
          new TokenAuthenticationFilter(tokenService),
          UsernamePasswordAuthenticationFilter.class
    );

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(
          "/v3/api-docs",
          "/v3/api-docs/**",
          "/swagger-resources/**",
          "/swagger-ui/**"
    );
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
              .allowedOrigins("http://localhost:3000")
              .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
              .allowedMethods("*")
              .allowedHeaders("*")
              .exposedHeaders("Authorization")
              .allowCredentials(true);
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}