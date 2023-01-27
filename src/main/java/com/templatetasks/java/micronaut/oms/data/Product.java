package com.templatetasks.java.micronaut.oms.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:21
 */
@Data
public class Product {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDateTime created;

    private LocalDateTime lastModified;
}
