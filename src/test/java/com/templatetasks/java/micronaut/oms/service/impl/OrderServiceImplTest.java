package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.api.OrderRequest;
import com.templatetasks.java.micronaut.oms.api.OrderRequestItem;
import com.templatetasks.java.micronaut.oms.data.Order;
import com.templatetasks.java.micronaut.oms.data.entity.CustomerEntity;
import com.templatetasks.java.micronaut.oms.data.entity.OrderEntity;
import com.templatetasks.java.micronaut.oms.data.entity.OrderItemEntity;
import com.templatetasks.java.micronaut.oms.data.entity.ProductEntity;
import com.templatetasks.java.micronaut.oms.service.OrderService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:13
 */
@MicronautTest
class OrderServiceImplTest {

    @Inject
    EntityManager em;

    @Inject
    OrderService service;

    @Test
    void getNotFound() {
        var id = 404L;
        assertNull(service.get(id));
    }

    @Test
    void getNotFoundForCustomer() {
        var id = 404404L;
        var results = service.getByCustomerId(id);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getByOrderIdAndCustomerId() {
        var customerEntity = new CustomerEntity();
        customerEntity.setEmail("john.doe@customers.com");
        customerEntity.setFirstName("John");
        customerEntity.setLastName("Doe");
        em.persist(customerEntity);

        var productEntity = new ProductEntity();
        productEntity.setName("Junk");
        productEntity.setDescription("Just simple junk, nothing special.");
        productEntity.setPrice(BigDecimal.valueOf(1.23));
        em.persist(productEntity);

        var orderEntity = new OrderEntity();
        orderEntity.setCustomerId(customerEntity.getId());
        em.persist(orderEntity);

        var orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrder(orderEntity);
        orderItemEntity.setProduct(productEntity);
        orderItemEntity.setQuantity(10L);
        em.persist(orderItemEntity);

        Collection<OrderItemEntity> orderItemEntities = new ArrayList<>();
        orderItemEntities.add(orderItemEntity);
        orderEntity.setItems(orderItemEntities);
        orderEntity = em.merge(orderEntity);

        em.getTransaction().commit();

        assertNotNull(orderEntity.getId());

        var order = service.get(orderEntity.getId());
        testOrder(order, customerEntity, productEntity, orderItemEntity.getQuantity());

        var orders = service.getByCustomerId(customerEntity.getId());
        assertNotNull(order);
        assertEquals(1, orders.size());

        order = orders.get(0);
        testOrder(order, customerEntity, productEntity, orderItemEntity.getQuantity());
    }

    @Test
    void createNoCustomer() {
        var createRequestItem = new OrderRequestItem();
        createRequestItem.setProductId(10L);
        createRequestItem.setQuantity(7L);

        var createRequest = new OrderRequest();
        createRequest.setItems(List.of(createRequestItem));

        var order = service.create(47L, createRequest);
        assertNull(order);
    }

    @Test
    void create() {
        var customerEntity = new CustomerEntity();
        customerEntity.setEmail("john2.doe2@customers.com");
        customerEntity.setFirstName("John2");
        customerEntity.setLastName("Doe2");
        em.persist(customerEntity);

        var productEntity = new ProductEntity();
        productEntity.setName("Rare Junk");
        productEntity.setDescription("Quite a rare junk, more expensive than usual one.");
        productEntity.setPrice(BigDecimal.valueOf(12.30));
        em.persist(productEntity);

        var createRequestItem = new OrderRequestItem();
        createRequestItem.setProductId(productEntity.getId());
        createRequestItem.setQuantity(7L);

        var createRequest = new OrderRequest();
        createRequest.setItems(List.of(createRequestItem));

        var order = service.create(customerEntity.getId(), createRequest);
        testOrder(order, customerEntity, productEntity, createRequestItem.getQuantity());
    }

/*
    @Test
    void update() {
        var customerEntity = new CustomerEntity();
        customerEntity.setEmail("john3.doe3@customers.com");
        customerEntity.setFirstName("John3");
        customerEntity.setLastName("Doe3");
        em.persist(customerEntity);

        var productEntity = new ProductEntity();
        productEntity.setName("Awesome Junk");
        productEntity.setDescription("Really awesome junk, more expensive than usual and rare ones.");
        productEntity.setPrice(BigDecimal.valueOf(123.00));
        em.persist(productEntity);

        var createRequestItem = new OrderRequestItem();
        createRequestItem.setProductId(productEntity.getId());
        createRequestItem.setQuantity(15L);

        var createRequest = new OrderRequest();
        createRequest.setItems(List.of(createRequestItem));

        var order = service.create(customerEntity.getId(), createRequest);
        testOrder(order, customerEntity, productEntity, createRequestItem.getQuantity());


    }
*/

    @Test
    void delete() {
        var id = 66L;
        service.delete(id);
    }

    private void testOrder(Order order, CustomerEntity customerEntity, ProductEntity productEntity, long expectedQuantity) {
        assertNotNull(order);
        assertEquals(customerEntity.getId(), order.getCustomerId());
        assertNotNull(order.getItems());
        assertEquals(1, order.getItems().size());

        var orderItem = order.getItems().iterator().next();
        assertNotNull(orderItem);
        assertEquals(expectedQuantity, orderItem.getQuantity());

        var product = orderItem.getProduct();
        assertNotNull(product);
        assertEquals(productEntity.getId(), product.getId());
    }
}