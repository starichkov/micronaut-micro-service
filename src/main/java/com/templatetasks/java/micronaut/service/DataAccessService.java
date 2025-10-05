package com.templatetasks.java.micronaut.service;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.List;

/**
 * @author Vadim Starichkov
 * @since 14.09.2022 15:29
 */
public interface DataAccessService<T, ID> {

    @Nullable
    T get(ID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    @Nullable
    T create(T entity);

    T update(ID id, T entity);

    void delete(ID id);
}
