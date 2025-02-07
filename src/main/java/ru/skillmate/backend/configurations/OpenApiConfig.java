package ru.skillmate.backend.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Skill Mate",
                description = "Api definition for backend",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}
