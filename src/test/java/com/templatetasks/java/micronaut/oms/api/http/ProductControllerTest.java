package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.data.Product;
import com.templatetasks.java.micronaut.oms.service.ProductService;
import com.templatetasks.java.micronaut.oms.service.impl.ProductServiceImpl;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 12:21
 */
@MicronautTest
class ProductControllerTest {

    @Inject
    @Client("/products")
    HttpClient client;

    @Inject
    ProductService productService;

    @Test
    void getProductById() {
        var id = 1L;

        var product = new Product();
        product.setId(id);
        product.setName("Junk");
        product.setDescription("Real nice piece of junk!");
        product.setPrice(BigDecimal.valueOf(999.66));

        when(productService.get(eq(id)))
                .thenReturn(product);

        Product response = client.toBlocking()
                                   .retrieve(HttpRequest.GET("/" + id), Product.class);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    void createProduct() {
        var id = 2L;

        var product = new Product();
        product.setName("Awesome Junk");
        product.setDescription("Really awesome and shiny piece of junk!");
        product.setPrice(BigDecimal.valueOf(100500.66));

        doAnswer(invocationOnMock -> {
            Product saved = invocationOnMock.getArgument(0);
            saved.setId(id);
            return saved;
        }).when(productService).create(eq(product));

        Product response = client.toBlocking()
                                   .retrieve(HttpRequest.POST("/", product), Product.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    void updateProduct() {
        var id = 3L;

        var product = new Product();
        product.setName("Superb Junk");
        product.setDescription("You'll never find better junk that this!");
        product.setPrice(BigDecimal.valueOf(100500.66));

        doAnswer(invocationOnMock -> {
            Long paramId = invocationOnMock.getArgument(0);
            Product paramProduct = invocationOnMock.getArgument(1);
            paramProduct.setId(paramId);
            return paramProduct;
        }).when(productService).update(eq(id), eq(product));

        Product response = client.toBlocking()
                                   .retrieve(HttpRequest.PATCH("/" + id, product), Product.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    void deleteProductById() {
        var id = 4L;

        HttpResponse<Void> response = client.toBlocking()
                                              .exchange(HttpRequest.DELETE("/" + id));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.code());
    }

    @MockBean(ProductServiceImpl.class)
    ProductService productService() {
        return mock(ProductService.class);
    }
}