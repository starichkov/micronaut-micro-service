package com.templatetasks.java.micronaut.oms.data.mapper;

import com.templatetasks.java.micronaut.oms.data.OrderItem;
import com.templatetasks.java.micronaut.oms.data.entity.OrderItemEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:08
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JSR330,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                ProductMapper.class
        }
)
public interface OrderItemMapper {

    OrderItem map(OrderItemEntity entity);
}
