package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.api.OrderRequest;
import com.templatetasks.java.micronaut.oms.data.Order;
import com.templatetasks.java.micronaut.oms.data.access.CustomerRepository;
import com.templatetasks.java.micronaut.oms.data.access.OrderRepository;
import com.templatetasks.java.micronaut.oms.data.access.ProductRepository;
import com.templatetasks.java.micronaut.oms.data.entity.OrderEntity;
import com.templatetasks.java.micronaut.oms.data.entity.OrderItemEntity;
import com.templatetasks.java.micronaut.oms.data.mapper.OrderMapper;
import com.templatetasks.java.micronaut.oms.service.OrderService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:26
 */
@Primary
@Singleton
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final OrderRepository repository;

    private final OrderMapper orderMapper;

    @Inject
    public OrderServiceImpl(CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository,
                            OrderMapper orderMapper
    ) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.repository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Nullable
    @Override
    @ReadOnly
    public Order get(Long id) {
        return repository.findById(id)
                         .map(orderMapper::map)
                         .orElse(null);
    }

    @Override
    @ReadOnly
    public List<Order> getByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                         .stream()
                         .map(orderMapper::map)
                         .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Order create(Long customerId, OrderRequest orderRequest) {
        if (!customerRepository.existsById(customerId)) {
            log.error("No such customer");
            return null;
        }

        Collection<OrderItemEntity> itemEntities = new ArrayList<>();

        final OrderEntity order = new OrderEntity();
        order.setCustomerId(customerId);
        order.setItems(itemEntities);

        orderRequest.getItems()
                    .forEach(item -> productRepository.findById(item.getProductId())
                                                       .ifPresent(
                                                               product -> {
                                                                   var itemEntity = new OrderItemEntity();
                                                                   itemEntity.setOrder(order);
                                                                   itemEntity.setProduct(product);
                                                                   itemEntity.setQuantity(item.getQuantity());
                                                                   itemEntities.add(itemEntity);
                                                               }
                                                       )
                     );

        OrderEntity saved = repository.save(order);
        return orderMapper.map(saved);
    }

    @Override
    public Order update(Long orderId, OrderRequest orderRequest) {
        return repository.findById(orderId)
                         .map(orderEntity -> doUpdate(orderEntity, orderRequest))
                         .map(repository::save)
                         .map(orderMapper::map)
                         .orElse(null);
    }

    private OrderEntity doUpdate(OrderEntity stored, OrderRequest orderRequest) {
        log.debug("Order request: {}", orderRequest);
        // TODO
        return stored;
    }

    @Override
    public void delete(Long orderId) {
        repository.deleteById(orderId);
    }
}
