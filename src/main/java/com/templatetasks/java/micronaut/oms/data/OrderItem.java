package com.templatetasks.java.micronaut.oms.data;

import lombok.Data;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 14:56
 */
@Data
public class OrderItem {

    private Product product;

    private long quantity;
}
