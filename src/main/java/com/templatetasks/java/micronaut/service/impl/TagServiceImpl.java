package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.data.access.TagRepository;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import com.templatetasks.java.micronaut.data.mapper.TagMapper;
import com.templatetasks.java.micronaut.service.TagService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:26
 */
@Primary
@Singleton
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    private final TagMapper tagMapper;

    @Inject
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.repository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Nullable
    @Override
    @ReadOnly
    public Tag get(Long id) {
        return repository.findById(id)
                       .map(tagMapper::map)
                       .orElse(null);
    }

    @Override
    @ReadOnly
    public List<Tag> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(tagMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ReadOnly
    public Page<Tag> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(tagMapper::map);
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        var entity = tagMapper.map(tag);
        entity = repository.save(entity);
        return tagMapper.map(entity);
    }

    @Override
    @Transactional
    public Tag update(Long id, Tag provided) {
        return repository.findById(id)
                       .map(stored -> doUpdate(stored, provided))
                       .map(repository::save)
                       .map(tagMapper::map)
                       .orElse(null);
    }

    private TagEntity doUpdate(TagEntity stored, Tag provided) {
        stored.setLabel(provided.getLabel());
        return stored;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
