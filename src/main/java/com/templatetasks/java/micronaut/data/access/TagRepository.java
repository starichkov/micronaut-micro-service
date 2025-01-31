package com.templatetasks.java.micronaut.data.access;

import com.templatetasks.java.micronaut.data.entity.TagEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 16:42
 */
@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long> {
}
