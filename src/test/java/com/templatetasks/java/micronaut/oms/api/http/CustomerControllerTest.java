package com.templatetasks.java.micronaut.oms.api.http;

import com.templatetasks.java.micronaut.oms.data.Customer;
import com.templatetasks.java.micronaut.oms.service.CustomerService;
import com.templatetasks.java.micronaut.oms.service.impl.CustomerServiceImpl;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 18.03.2023 22:36
 */
@MicronautTest
class CustomerControllerTest {

    @Inject
    @Client("/customers")
    HttpClient client;

    @Inject
    CustomerService customerService;

    @Test
    void getCustomerById() {
        var id = 1L;

        var customer = new Customer();
        customer.setId(id);
        customer.setFirstName("F");
        customer.setLastName("L");
        customer.setEmail("f_l@company.com");

        when(customerService.get(eq(id)))
                .thenReturn(customer);

        Customer response = client.toBlocking()
                                    .retrieve(HttpRequest.GET("/" + id), Customer.class);

        assertNotNull(response);
        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getFirstName(), response.getFirstName());
        assertEquals(customer.getLastName(), response.getLastName());
        assertEquals(customer.getEmail(), response.getEmail());
    }

    @Test
    void createCustomer() {
        var id = 2L;

        var customer = new Customer();
        customer.setFirstName("F2");
        customer.setLastName("L2");
        customer.setEmail("f2_l2@company.com");

        doAnswer(invocationOnMock -> {
            Customer saved = invocationOnMock.getArgument(0);
            saved.setId(id);
            return saved;
        }).when(customerService).create(eq(customer));

        Customer response = client.toBlocking()
                                    .retrieve(HttpRequest.POST("/", customer), Customer.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(customer.getFirstName(), response.getFirstName());
        assertEquals(customer.getLastName(), response.getLastName());
        assertEquals(customer.getEmail(), response.getEmail());
    }

    @Test
    void updateCustomer() {
        var id = 3L;

        var customer = new Customer();
        customer.setFirstName("F3");
        customer.setLastName("L3");
        customer.setEmail("f3_l3@company.com");

        doAnswer(invocationOnMock -> {
            Long paramId = invocationOnMock.getArgument(0);
            Customer paramCustomer = invocationOnMock.getArgument(1);
            paramCustomer.setId(paramId);
            return paramCustomer;
        }).when(customerService).update(eq(id), eq(customer));

        Customer response = client.toBlocking()
                                    .retrieve(HttpRequest.PATCH("/" + id, customer), Customer.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(customer.getFirstName(), response.getFirstName());
        assertEquals(customer.getLastName(), response.getLastName());
        assertEquals(customer.getEmail(), response.getEmail());
    }

    @Test
    void deleteCustomerById() {
        var id = 4L;

        HttpResponse<Void> response = client.toBlocking()
                                              .exchange(HttpRequest.DELETE("/" + id));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.code());
    }

    @MockBean(CustomerServiceImpl.class)
    CustomerService customerService() {
        return mock(CustomerService.class);
    }
}