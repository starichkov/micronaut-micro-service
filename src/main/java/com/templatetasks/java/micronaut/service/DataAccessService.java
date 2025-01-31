package com.templatetasks.java.micronaut.service;

import io.micronaut.core.annotation.Nullable;

/**
 * @author Vadim Starichkov
 * @since 14.09.2022 15:29
 */
public interface DataAccessService<T, ID> {

    @Nullable
    T get(ID id);

    @Nullable
    T create(T entity);

    T update(ID id, T entity);

    void delete(ID id);
}
