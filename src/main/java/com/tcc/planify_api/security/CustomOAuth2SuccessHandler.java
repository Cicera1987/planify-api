package com.tcc.planify_api.security;

import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final TokenService tokenService;
  private final UserService userService;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${frontend.url.dev:#{null}}")
  private String frontendUrlDev;

  public CustomOAuth2SuccessHandler(TokenService tokenService, UserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @Override
  public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException {

    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    var attributes = oauthToken.getPrincipal().getAttributes();

    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");
    String picture = (String) attributes.get("picture");

    UserEntity user = userService.findOrCreateByEmail(email, name, picture);
    String jwt = tokenService.generateToken(user);

    String origin = request.getHeader("Origin");
    String redirectUrl;

    if (origin != null && origin.contains("planify-web-dev.onrender.com") && frontendUrlDev != null) {
      redirectUrl = frontendUrlDev;
    } else {
      redirectUrl = frontendUrl;
    }

    response.sendRedirect(redirectUrl + "/oauth/success?token=" + jwt);
  }
}