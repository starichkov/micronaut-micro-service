package com.templatetasks.java.micronaut.api;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:23
 */
@Data
public class NoteTag {

    @NotNull
    @Positive
    private long noteId;

    @NotNull
    @Positive
    private long tagId;
}
