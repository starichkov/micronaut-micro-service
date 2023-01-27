package com.templatetasks.java.micronaut.oms.data.mapper;

import com.templatetasks.java.micronaut.oms.data.Product;
import com.templatetasks.java.micronaut.oms.data.entity.ProductEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:25
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JSR330,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductMapper {

    ProductEntity map(Product product);

    Product map(ProductEntity productEntity);
}
