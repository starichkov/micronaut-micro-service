package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.access.NoteRepository;
import com.templatetasks.java.micronaut.data.access.TagRepository;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import com.templatetasks.java.micronaut.data.mapper.NoteMapper;
import com.templatetasks.java.micronaut.service.NoteService;
import com.templatetasks.java.micronaut.service.exception.NotFoundException;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:21
 */
@Primary
@Singleton
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;
    private final TagRepository tagRepository;
    private final NoteMapper noteMapper;

    @Inject
    public NoteServiceImpl(NoteRepository noteRepository, TagRepository tagRepository, NoteMapper noteMapper) {
        this.repository = noteRepository;
        this.tagRepository = tagRepository;
        this.noteMapper = noteMapper;
    }

    @Nullable
    @Override
    @NewSpan("note-service-get")
    public Note get(Long id) {
        return repository.findById(id)
                         .map(noteMapper::map)
                         .orElse(null);
    }

    @Override
    @NewSpan("note-service-find-all")
    public List<Note> findAll() {
        return repository.findAll().stream()
                         .map(noteMapper::map)
                         .collect(Collectors.toList());
    }

    @Override
    @ContinueSpan
    public Page<Note> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                         .map(noteMapper::map);
    }

    @Override
    @NewSpan("note-service-create")
    public Note create(Note note) {
        var entity = noteMapper.map(note);
        entity = repository.save(entity);
        return noteMapper.map(entity);
    }

    @Override
    @NewSpan("note-service-update")
    public Note update(Long noteId, Note provided) {
        return repository.findById(noteId)
                         .map(stored -> doUpdate(stored, provided))
                         .map(repository::save)
                         .map(noteMapper::map)
                         .orElse(null);
    }

    private NoteEntity doUpdate(NoteEntity stored, Note provided) {
        stored.setTitle(provided.getTitle());
        stored.setContent(provided.getContent());
        return stored;
    }

    @Override
    @NewSpan("note-service-delete")
    public void delete(Long productId) {
        repository.deleteById(productId);
    }

    @Override
    @NewSpan("note-service-add-tag")
    public Note addTag(Long noteId, Long tagId) {
        var note = getById(noteId);
        var tag = getTagById(tagId);

        if (note.getTags().contains(tag)) {
            return noteMapper.map(note);
        }

        note.addTag(tag);
        note = repository.save(note);

        return noteMapper.map(note);
    }

    @Override
    @NewSpan("note-service-remove-tag")
    public Note removeTag(Long noteId, Long tagId) {
        var note = getById(noteId);
        var tag = getTagById(tagId);

        if (!note.getTags().contains(tag)) {
            return noteMapper.map(note);
        }

        note.removeTag(tag);
        note = repository.save(note);

        return noteMapper.map(note);
    }

    /**
     * Get note by ID
     *
     * @param id note ID
     * @return note entity
     * @throws NotFoundException if note not found
     */
    private NoteEntity getById(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new NotFoundException(String.format("Note with ID %d not found", id)));
    }

    /**
     * Get tag by ID
     *
     * @param id tag ID
     * @return tag entity
     * @throws NotFoundException if tag not found
     */
    private TagEntity getTagById(Long id) {
        return tagRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(String.format("Tag with ID %d not found", id)));
    }
}
