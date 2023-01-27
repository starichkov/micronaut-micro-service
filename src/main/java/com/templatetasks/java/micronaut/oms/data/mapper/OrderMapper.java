package com.templatetasks.java.micronaut.oms.data.mapper;

import com.templatetasks.java.micronaut.oms.data.Order;
import com.templatetasks.java.micronaut.oms.data.entity.OrderEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:36
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JSR330,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                OrderItemMapper.class,
                ProductMapper.class
        }
)
public interface OrderMapper {

    OrderEntity map(Order order);

    Order map(OrderEntity orderEntity);
}
