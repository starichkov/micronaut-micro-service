package com.templatetasks.java.micronaut.oms.service;

import com.templatetasks.java.micronaut.oms.api.OrderRequest;
import com.templatetasks.java.micronaut.oms.data.Order;
import io.micronaut.core.annotation.Nullable;

import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:25
 */
public interface OrderService {

    @Nullable
    Order get(Long orderId);

    List<Order> getByCustomerId(Long customerId);

    @Nullable
    Order create(Long customerId, OrderRequest orderRequest);

    Order update(Long orderId, OrderRequest orderRequest);

    void delete(Long orderId);
}
