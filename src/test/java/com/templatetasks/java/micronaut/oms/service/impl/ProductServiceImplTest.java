package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.data.Product;
import com.templatetasks.java.micronaut.oms.data.entity.ProductEntity;
import com.templatetasks.java.micronaut.oms.service.ProductService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 13:09
 */
@MicronautTest
class ProductServiceImplTest {

    @Inject
    EntityManager em;

    @Inject
    ProductService service;

    @Test
    void getNotFound() {
        var id = 404L;
        assertNull(service.get(id));
    }

    @Test
    void get() {
        var entity = new ProductEntity();
        entity.setName("Awesome Junk");
        entity.setDescription("Really awesome and shiny piece of junk!");
        entity.setPrice(BigDecimal.valueOf(100500.66));

        entity = em.merge(entity);
        em.getTransaction().commit();

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var product = service.get(id);
        assertNotNull(product);
        assertEquals(entity.getId(), product.getId());
        assertEquals(entity.getName(), product.getName());
        assertEquals(entity.getDescription(), product.getDescription());
        assertEquals(entity.getPrice(), product.getPrice());
        assertEquals(entity.getCreated(), product.getCreated());
        assertEquals(entity.getLastModified(), product.getLastModified());
    }

    @Test
    void create() {
        var product = new Product();
        product.setName("Awesome Junk");
        product.setDescription("Really awesome and shiny piece of junk!");
        product.setPrice(BigDecimal.valueOf(100500.66));

        var created = service.create(product);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(product.getName(), created.getName());
        assertEquals(product.getDescription(), created.getDescription());
        assertEquals(product.getPrice(), created.getPrice());
        assertNotNull(created.getCreated());
        assertNotNull(created.getLastModified());
    }

    @Test
    void updateNotFound() {
        var id = 3L;

        var product = new Product();
        product.setName("Junk");
        product.setDescription("Real nice piece of junk!");
        product.setPrice(BigDecimal.valueOf(999.66));

        var updated = service.update(id, product);
        assertNull(updated);
    }

    @Test
    void update() {
        var entity = new ProductEntity();
        entity.setName("Awesome Junk");
        entity.setDescription("Really awesome and shiny piece of junk!");
        entity.setPrice(BigDecimal.valueOf(100500.66));
        entity = em.merge(entity);
        em.getTransaction().commit();
        assertNotNull(entity);

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var product = new Product();
        product.setName("Superb Junk");
        product.setDescription("You'll never find better junk that this!");
        product.setPrice(BigDecimal.valueOf(100500.66));

        var updated = service.update(id, product);
        assertNotNull(updated);
        assertEquals(id, updated.getId());
        assertEquals(product.getName(), updated.getName());
        assertEquals(product.getDescription(), updated.getDescription());
        assertEquals(product.getPrice(), updated.getPrice());
    }

    @Test
    void delete() {
        var id = 66L;
        service.delete(id);
    }
}