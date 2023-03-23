package com.templatetasks.java.micronaut.oms.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 20.03.2023 14:50
 */
@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class OrderItemEntity implements Serializable {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrderEntity order;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
