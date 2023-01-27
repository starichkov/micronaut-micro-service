package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.api.CreateOrderRequest;
import com.templatetasks.java.micronaut.oms.data.Order;
import com.templatetasks.java.micronaut.oms.service.OrderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/orders")
@ExecuteOn(TaskExecutors.IO)
public class OrderController {

    private final OrderService service;

    @Inject
    public OrderController(OrderService orderService) {
        this.service = orderService;
    }

    @Get("/{id}")
    public Order getOrderById(Long id) {
        return service.get(id);
    }

    @Get("/user/{customerId}")
    public List<Order> getOrdersByUserId(Long customerId) {
        return service.getByCustomerId(customerId);
    }

    @Post("/{customerId}")
    public Order create(Long customerId, CreateOrderRequest createRequest) {
        return service.create(customerId, createRequest);
    }
}
