package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.access.NoteRepository;
import com.templatetasks.java.micronaut.data.access.TagRepository;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import com.templatetasks.java.micronaut.data.mapper.NoteMapper;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public Note get(Long id) {
        return repository.findById(id)
                         .map(noteMapper::map)
                         .orElse(null);
    }

    @Override
    public List<Note> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(noteMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public Note create(Note note) {
        var entity = noteMapper.map(note);
        entity = repository.save(entity);
        return noteMapper.map(entity);
    }

    @Override
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
    public void delete(Long productId) {
        repository.deleteById(productId);
    }

    @Override
    public Note addTag(Long noteId, Long tagId) {
        var noteOptional = repository.findById(noteId);
        var tagOptional = tagRepository.findById(tagId);

        if (noteOptional.isEmpty() || tagOptional.isEmpty()) {
            return null;
        }

        var note = noteOptional.get();
        var tag = tagOptional.get();

        note.addTag(tag);
        note = repository.save(note);

        return noteMapper.map(note);
    }

    @Override
    public Note removeTag(Long noteId, Long tagId) {
        var noteOptional = repository.findById(noteId);
        var tagOptional = tagRepository.findById(tagId);

        if (noteOptional.isEmpty() || tagOptional.isEmpty()) {
            return null;
        }

        var note = noteOptional.get();
        var tag = tagOptional.get();

        note.removeTag(tag);
        note = repository.save(note);

        return noteMapper.map(note);
    }
}
