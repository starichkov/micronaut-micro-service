package com.templatetasks.java.micronaut.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:21
 */
@Data
public class Note {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime created;

    private LocalDateTime lastModified;
}
