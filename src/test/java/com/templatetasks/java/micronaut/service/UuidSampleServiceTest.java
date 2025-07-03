package com.templatetasks.java.micronaut.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UuidSampleServiceTest {

    @Inject
    SampleService sampleService;

    @Test
    void testGetSample() {
        String result = sampleService.getSample();

        assertNotNull(result);

        UUID uuid = UUID.fromString(result);
        assertNotNull(uuid);
    }

    @Test
    void testGetSampleReturnsUniqueValues() {
        String result1 = sampleService.getSample();
        String result2 = sampleService.getSample();

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1, result2, "Each call should return a unique UUID");
    }
}