package com.templatetasks.java.micronaut.api.http;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the HealthController class
 */
@MicronautTest(startApplication = false)
class HealthControllerTest {

    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;
    
    private HealthController healthController;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        healthController = new HealthController(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    void testHealthWhenDatabaseIsUp() throws SQLException {
        // Setup mock connection that returns valid
        when(connection.isValid(anyInt())).thenReturn(true);

        // Call the health method directly
        Map<String, Object> response = healthController.health();

        // Verify response
        assertNotNull(response);
        assertEquals("UP", response.get("status"));
        assertNotNull(response.get("timestamp"));
        
        Map<String, Object> dbHealth = (Map<String, Object>) response.get("database");
        assertNotNull(dbHealth);
        assertEquals("UP", dbHealth.get("status"));
        
        // Verify the connection was closed
        verify(connection).close();
    }

    @Test
    void testHealthWhenDatabaseIsDown() throws SQLException {
        // Setup mock connection that returns invalid
        when(connection.isValid(anyInt())).thenReturn(false);

        // Call the health method directly
        Map<String, Object> response = healthController.health();

        // Verify response
        assertNotNull(response);
        assertEquals("DOWN", response.get("status"));
        assertNotNull(response.get("timestamp"));
        
        Map<String, Object> dbHealth = (Map<String, Object>) response.get("database");
        assertNotNull(dbHealth);
        assertEquals("DOWN", dbHealth.get("status"));
        assertEquals("Database connection is not valid", dbHealth.get("error"));
        
        // Verify the connection was closed
        verify(connection).close();
    }

    @Test
    void testHealthWhenDatabaseThrowsSQLException() throws SQLException {
        // Setup mock connection that throws SQLException
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        // Call the health method directly
        Map<String, Object> response = healthController.health();

        // Verify response
        assertNotNull(response);
        assertEquals("DOWN", response.get("status"));
        assertNotNull(response.get("timestamp"));
        
        Map<String, Object> dbHealth = (Map<String, Object>) response.get("database");
        assertNotNull(dbHealth);
        assertEquals("DOWN", dbHealth.get("status"));
        assertEquals("Database connection failed", dbHealth.get("error"));
    }
}