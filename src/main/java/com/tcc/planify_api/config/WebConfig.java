package com.tcc.planify_api.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${CLOUDINARY_CLOUD_NAME}")
  private String cloudName;

  @Value("${CLOUDINARY_API_KEY}")
  private String apiKey;

  @Value("${CLOUDINARY_API_SECRET}")
  private String apiSecret;

  @Bean
  public Cloudinary cloudinary() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", apiKey);
    config.put("api_secret", apiSecret);
    config.put("secure", "true");
    return new Cloudinary(config);
  }
}
