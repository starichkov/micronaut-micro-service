package com.templatetasks.java.micronaut.api.http;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the HealthController class
 *
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 13.07.2025 19:16
 */
@MicronautTest
class HealthTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void healthEndpointExposed() {
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus.class);
        assertEquals(HttpStatus.OK, status);
    }
}