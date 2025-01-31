package com.templatetasks.java.micronaut.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:20
 */
@Data
public class Tag {

    private Long id;

    private String label;

    private LocalDateTime created;

    private LocalDateTime lastModified;
}
