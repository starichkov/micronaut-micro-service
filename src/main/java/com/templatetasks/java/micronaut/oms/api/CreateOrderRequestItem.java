package com.templatetasks.java.micronaut.oms.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:23
 */
@Data
public class CreateOrderRequestItem {

    @NotNull
    @Positive
    private long productId;

    @NotNull
    @Positive
    private long quantity;
}
