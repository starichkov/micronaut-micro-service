package com.templatetasks.java.micronaut.oms.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:20
 */
@Data
public class Customer {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime created;

    private LocalDateTime lastModified;

    private Collection<Order> orders;
}
