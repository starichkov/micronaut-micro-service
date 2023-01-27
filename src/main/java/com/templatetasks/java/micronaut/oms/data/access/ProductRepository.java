package com.templatetasks.java.micronaut.oms.data.access;

import com.templatetasks.java.micronaut.oms.data.entity.ProductEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 16:43
 */
@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
