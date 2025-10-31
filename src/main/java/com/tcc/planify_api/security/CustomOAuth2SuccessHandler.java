package com.tcc.planify_api.security;

import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final TokenService tokenService;
  private final UserService userService;
  private final Environment environment;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${frontend.url.local}")
  private String frontendUrlLocal;

  @Value("${frontend.url.prod}")
  private String frontendUrlProd;

  public CustomOAuth2SuccessHandler(
        @Lazy TokenService tokenService,
        @Lazy UserService userService,
        Environment environment) {
    this.tokenService = tokenService;
    this.userService = userService;
    this.environment = environment;
  }

  @Override
  public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
  ) throws IOException, ServletException {

    var oauthToken = (OAuth2AuthenticationToken) authentication;
    var attributes = oauthToken.getPrincipal().getAttributes();

    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");
    String picture = (String) attributes.get("picture");

    UserEntity user = userService.findOrCreateByEmail(email, name, picture);
    String jwt = tokenService.generateToken(user);

    String activeProfile = environment.getProperty("spring.profiles.active", "dev");

    String redirectBaseUrl = switch (activeProfile) {
      case "prod" -> frontendUrlProd;
      case "local" -> frontendUrlLocal;
      default -> frontendUrl;
    };

    response.sendRedirect(redirectBaseUrl + "/oauth/success?token=" + jwt);
  }
}