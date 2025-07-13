package com.templatetasks.java.micronaut.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:21
 */
@Data
public class Note {

    private Long id;

    private String title;

    private String content;

    private Set<Tag> tags;

    private LocalDateTime created;

    private LocalDateTime lastModified;
}
