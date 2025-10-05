package com.templatetasks.java.micronaut.service.exception;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 05.10.2025 16:23
 */
public final class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
