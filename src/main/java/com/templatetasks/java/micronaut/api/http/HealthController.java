package com.templatetasks.java.micronaut.api.http;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for monitoring application health
 */
@Controller("/health")
@ExecuteOn(TaskExecutors.IO)
public class HealthController {

    private final DataSource dataSource;

    @Inject
    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());

        // Check database connection
        Map<String, Object> dbHealth = checkDatabaseHealth();
        health.put("database", dbHealth);

        if ("DOWN".equals(dbHealth.get("status"))) {
            health.put("status", "DOWN");
        }

        return health;
    }

    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        dbHealth.put("status", "UP");

        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(5)) {
                dbHealth.put("status", "DOWN");
                dbHealth.put("error", "Database connection is not valid");
            }
        } catch (SQLException e) {
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
        }

        return dbHealth;
    }
}
