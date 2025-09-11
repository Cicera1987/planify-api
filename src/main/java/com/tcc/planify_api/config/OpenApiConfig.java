package com.tcc.planify_api.config;
import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.enums.PositionEnum;
import com.tcc.planify_api.repository.PositionRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    private final PositionRepository positionRepository;

  public OpenApiConfig(PositionRepository positionRepository) {
    this.positionRepository = positionRepository;
  }

  @Bean
    public OpenAPI springShopOpenAPI() {
      return new OpenAPI()
            .info(new Info()
                  .title("Planify API")
                  .description("Documentação da API Planify com autenticação JWT")
                  .version("v1.0.0")
                  .license(new License().name("Apache 2.0").url("https://springdoc.org"))
            )
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                  .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                              .name(SECURITY_SCHEME_NAME)
                              .type(SecurityScheme.Type.HTTP)
                              .scheme("bearer")
                              .bearerFormat("JWT")
                  )
            );
    }

  @Bean
  public CommandLineRunner loadPositions() {
    return args -> {
      for (PositionEnum pos : PositionEnum.values()) {
        if (!positionRepository.existsByPosition(pos)) {
          positionRepository.save(PositionEntity.builder().position(pos).build());
        }
      }
    };
  }
}

