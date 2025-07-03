package com.templatetasks.java.micronaut.controller;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SampleAsyncControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testAsyncSampleGet() {
        var name = "test";

        String response = client.toBlocking().retrieve("/async/sample/" + name, String.class);

        assertTrue(response.startsWith(name + ":"));
        assertEquals(name.length() + 1 + 36, response.length());
    }
}