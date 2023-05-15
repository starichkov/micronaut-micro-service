package com.templatetasks.java.micronaut.oms.api;

import com.templatetasks.java.micronaut.oms.api.validation.UniqueProductPerOperationConstraint;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 16:20
 */
@Data
public class OrderRequest {

    @NotNull
    @NotEmpty
    @UniqueProductPerOperationConstraint
    private Map<@NotNull OrderRequestItemsOperation, @NotNull OrderRequestItems> items;
}
