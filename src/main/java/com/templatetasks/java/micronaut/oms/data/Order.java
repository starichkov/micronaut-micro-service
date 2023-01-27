package com.templatetasks.java.micronaut.oms.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:21
 */
@Data
public class Order {

    private Long id;

    private Long customerId;

    private Collection<OrderItem> items;

    private LocalDateTime created;

    private LocalDateTime lastModified;

    private LocalDateTime shipped;
}
