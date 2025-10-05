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

    /**
     * Paginated retrieval.
     */
    default Page<T> findAll(Pageable pageable) {
        List<T> all = findAll();
        // Basic in-memory pagination as a default; implementations should override for efficiency
        int offset = (int) Math.min((long) pageable.getNumber() * pageable.getSize(), all.size());
        int end = Math.min(offset + pageable.getSize(), all.size());
        List<T> content = all.subList(offset, end);
        return Page.of(content, pageable, (long) all.size());
    }

    @Nullable
    T create(T entity);

    T update(ID id, T entity);

    void delete(ID id);
}
