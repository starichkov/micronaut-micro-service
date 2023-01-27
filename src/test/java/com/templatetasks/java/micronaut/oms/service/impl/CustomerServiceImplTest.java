package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.data.Customer;
import com.templatetasks.java.micronaut.oms.data.entity.CustomerEntity;
import com.templatetasks.java.micronaut.oms.service.CustomerService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 12:29
 */
@MicronautTest
class CustomerServiceImplTest {

    @Inject
    EntityManager em;

    @Inject
    CustomerService service;

    @Test
    void getNotFound() {
        var id = 404L;
        assertNull(service.get(id));
    }

    @Test
    void get() {
        var entity = new CustomerEntity();
        entity.setFirstName("F");
        entity.setLastName("L");
        entity.setEmail("f_l@company.com");

        entity = em.merge(entity);
        em.getTransaction().commit();

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var customer = service.get(id);
        assertNotNull(customer);
        checkEquals(entity, customer);
    }

    @Test
    void create() {
        var customer = new Customer();
        customer.setFirstName("F2");
        customer.setLastName("L2");
        customer.setEmail("f2_l2@company.com");

        var created = service.create(customer);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(customer.getEmail(), created.getEmail());
        assertEquals(customer.getFirstName(), created.getFirstName());
        assertEquals(customer.getLastName(), created.getLastName());
        assertNotNull(created.getCreated());
        assertNotNull(created.getLastModified());
    }

    @Test
    void updateNotFound() {
        var id = 3L;

        var customer = new Customer();
        customer.setFirstName("F3-1");
        customer.setLastName("L3-1");
        customer.setEmail("f3_1_l3_1@company.com");

        var updated = service.update(id, customer);
        assertNull(updated);
    }

    @Test
    void update() {
        var entity = new CustomerEntity();
        entity.setFirstName("F3");
        entity.setLastName("L3");
        entity.setEmail("f3_l3@company.com");
        entity = em.merge(entity);
        em.getTransaction().commit();
        assertNotNull(entity);

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var customer = new Customer();
        customer.setFirstName("F3-1");
        customer.setLastName("L3-1");
        customer.setEmail("f3_1_l3_1@company.com");

        var updated = service.update(id, customer);
        assertNotNull(updated);
        assertEquals(id, updated.getId());
        assertEquals(customer.getEmail(), updated.getEmail());
        assertEquals(customer.getFirstName(), updated.getFirstName());
        assertEquals(customer.getLastName(), updated.getLastName());
    }

    @Test
    void delete() {
        var id = 66L;
        service.delete(id);
    }

    private void checkEquals(CustomerEntity entity, Customer customer) {
        assertEquals(entity.getId(), customer.getId());
        assertEquals(entity.getEmail(), customer.getEmail());
        assertEquals(entity.getFirstName(), customer.getFirstName());
        assertEquals(entity.getLastName(), customer.getLastName());
        assertEquals(entity.getCreated(), customer.getCreated());
        assertEquals(entity.getLastModified(), customer.getLastModified());
    }
}