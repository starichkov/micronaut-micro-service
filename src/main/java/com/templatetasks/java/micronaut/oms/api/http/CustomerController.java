package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.data.Customer;
import com.templatetasks.java.micronaut.oms.service.CustomerService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/customers")
@ExecuteOn(TaskExecutors.IO)
public class CustomerController {

    private final CustomerService service;

    @Inject
    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @Get("/{id}")
    public Customer getCustomerById(Long id) {
        return service.get(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Customer createCustomer(@Body Customer customer) {
        return service.create(customer);
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Customer updateCustomer(@PathVariable("id") Long id, @Body Customer customer) {
        return service.update(id, customer);
    }

    @Delete("/{id}")
    public void deleteCustomerById(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
