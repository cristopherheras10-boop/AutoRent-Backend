package com.autorent.api.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI openAPI() {
        String s="bearerAuth";
        return new OpenAPI().info(new Info().title("AutoRent API").version("1.0").description("API REST para alquiler de vehiculos")).addSecurityItem(new SecurityRequirement().addList(s)).components(new Components().addSecuritySchemes(s,new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
