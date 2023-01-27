package com.templatetasks.java.micronaut.oms.service.impl;

import com.templatetasks.java.micronaut.oms.data.Customer;
import com.templatetasks.java.micronaut.oms.data.access.CustomerRepository;
import com.templatetasks.java.micronaut.oms.data.entity.CustomerEntity;
import com.templatetasks.java.micronaut.oms.data.mapper.CustomerMapper;
import com.templatetasks.java.micronaut.oms.service.CustomerService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:26
 */
@Primary
@Singleton
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    private final CustomerMapper customerMapper;

    @Inject
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.repository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Nullable
    @Override
    @ReadOnly
    public Customer get(Long id) {
        return repository.findById(id)
                         .map(customerMapper::map)
                         .orElse(null);
    }

    @Override
    @Transactional
    public Customer create(Customer customer) {
        var entity = customerMapper.map(customer);
        entity = repository.save(entity);
        return customerMapper.map(entity);
    }

    @Override
    @Transactional
    public Customer update(Long customerId, Customer provided) {
        return repository.findById(customerId)
                         .map(stored -> doUpdate(stored, provided))
                         .map(repository::save)
                         .map(customerMapper::map)
                         .orElse(null);
    }

    private CustomerEntity doUpdate(CustomerEntity stored, Customer provided) {
        stored.setFirstName(provided.getFirstName());
        stored.setLastName(provided.getLastName());
        stored.setEmail(provided.getEmail());
        return stored;
    }

    @Override
    @Transactional
    public void delete(Long customerId) {
        repository.deleteById(customerId);
    }
}
