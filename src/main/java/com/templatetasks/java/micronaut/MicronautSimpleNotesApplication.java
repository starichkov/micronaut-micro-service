package com.templatetasks.java.micronaut;

import io.micronaut.runtime.Micronaut;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 09.09.2022 18:30
 */
public class MicronautSimpleNotesApplication {

    public static void main(String[] args) {
        Micronaut.run(MicronautSimpleNotesApplication.class, args);
    }
}
