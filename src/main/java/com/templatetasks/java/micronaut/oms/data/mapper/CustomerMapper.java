package com.templatetasks.java.micronaut.oms.data.mapper;

import com.templatetasks.java.micronaut.oms.data.Customer;
import com.templatetasks.java.micronaut.oms.data.entity.CustomerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:33
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JSR330,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CustomerMapper {

    CustomerEntity map(Customer customer);

    Customer map(CustomerEntity customerEntity);
}
