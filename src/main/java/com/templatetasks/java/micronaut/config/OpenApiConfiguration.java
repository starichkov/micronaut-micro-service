package com.templatetasks.java.micronaut.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Centralized OpenAPI/Swagger configuration to keep the main application class minimal.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Micro Service - Simple Notes",
                version = "v1",
                description = "Simple Notes service API"
        )
)
public final class OpenApiConfiguration {
    private OpenApiConfiguration() {
        // no-op: configuration holder for OpenAPI annotations
    }
}
