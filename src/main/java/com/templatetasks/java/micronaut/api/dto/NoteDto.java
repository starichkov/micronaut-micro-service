package com.templatetasks.java.micronaut.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * API DTO for Note exposure
 */
public record NoteDto(
        Long id,
        String title,
        String content,
        Set<TagDto> tags,
        LocalDateTime created,
        LocalDateTime lastModified
) {
}