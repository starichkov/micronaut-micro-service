package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.service.OrderService;
import com.templatetasks.java.micronaut.oms.service.impl.OrderServiceImpl;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 14:03
 */
@MicronautTest
class OrderControllerTest {

    @Inject
    @Client("/orders")
    HttpClient client;

    @Inject
    OrderService orderService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void getOrdersByUserId() {
    }

    @Test
    void create() {
    }

    @MockBean(OrderServiceImpl.class)
    OrderService orderService() {
        return mock(OrderService.class);
    }
}