package com.templatetasks.java.micronaut.oms.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 15.05.2023 16:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestItems {

    @NotNull
    @NotEmpty
    private List<@NotNull OrderRequestItem> items;

    public static OrderRequestItems create(OrderRequestItem... items) {
        return new OrderRequestItems(Arrays.asList(items));
    }
}
