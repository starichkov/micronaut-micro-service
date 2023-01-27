package com.templatetasks.java.micronaut.oms.api;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:20
 */
@Data
public class CreateOrderRequest {

    @NotNull
    @NotEmpty
    private List<@NotNull CreateOrderRequestItem> items;
}
