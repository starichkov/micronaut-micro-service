package com.templatetasks.java.micronaut.api.dto;

import java.time.LocalDateTime;

/**
 * API DTO for Tag exposure
 */
public record TagDto(
        Long id,
        String label,
        LocalDateTime created,
        LocalDateTime lastModified
) {
}