package com.templatetasks.java.micronaut.oms.data.access;

import com.templatetasks.java.micronaut.oms.data.entity.OrderEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 16:35
 */
@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomerId(Long customerId);
}
