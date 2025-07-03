package com.templatetasks.java.micronaut.controller;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SampleControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testSampleGet() {
        var name = "test";

        String response = client.toBlocking().retrieve("/sample/" + name, String.class);

        assertTrue(response.startsWith(name + ":"));
        assertTrue(response.length() > name.length() + 1);
    }
}