package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.data.Product;
import com.templatetasks.java.micronaut.oms.service.ProductService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:24
 */
@Controller("/products")
@ExecuteOn(TaskExecutors.IO)
public class ProductController {

    private final ProductService service;

    @Inject
    public ProductController(ProductService productService) {
        this.service = productService;
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public Product getProductById(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Product createProduct(@Body Product product) {
        return service.create(product);
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Product updateProduct(@PathVariable("id") Long id, @Body Product product) {
        return service.update(id, product);
    }

    @Delete("/{id}")
    public void deleteProductById(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
