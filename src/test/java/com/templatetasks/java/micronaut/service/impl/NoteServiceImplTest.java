package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 13:09
 */
@MicronautTest
class NoteServiceImplTest {

    @Inject
    EntityManager em;

    @Inject
    NoteService service;

    @Test
    void getNotFound() {
        var id = 404L;
        assertNull(service.get(id));
    }

    @Test
    void get() {
        var note = new NoteEntity();
        note.setTitle("Awesome Junk");
        note.setContent("Really awesome and shiny piece of junk!");

        note = em.merge(note);
        em.getTransaction().commit();

        var id = note.getId();
        assertNotNull(id);
        assertNotNull(note.getCreated());
        assertNotNull(note.getLastModified());
        assertEquals(0, note.getVersion());

        var result = service.get(id);
        assertNotNull(result);
        assertEquals(note.getId(), result.getId());
        assertEquals(note.getTitle(), result.getTitle());
        assertEquals(note.getContent(), result.getContent());
        assertEquals(note.getCreated(), result.getCreated());
        assertEquals(note.getLastModified(), result.getLastModified());
    }

    @Test
    void create() {
        var note = new Note();
        note.setTitle("Awesome Junk");
        note.setContent("Really awesome and shiny piece of junk!");

        var result = service.create(note);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(note.getTitle(), result.getTitle());
        assertEquals(note.getContent(), result.getContent());
        assertNotNull(result.getCreated());
        assertNotNull(result.getLastModified());
    }

    @Test
    void updateNotFound() {
        var id = 3L;

        var note = new Note();
        note.setTitle("Junk");
        note.setContent("Real nice piece of junk!");

        var updated = service.update(id, note);
        assertNull(updated);
    }

    @Test
    void update() {
        var entity = new NoteEntity();
        entity.setTitle("Awesome Junk");
        entity.setContent("Really awesome and shiny piece of junk!");
        entity = em.merge(entity);
        em.getTransaction().commit();
        assertNotNull(entity);

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var note = new Note();
        note.setTitle("Superb Junk");
        note.setContent("You'll never find better junk that this!");

        var result = service.update(id, note);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(note.getTitle(), result.getTitle());
        assertEquals(note.getContent(), result.getContent());
    }

    @Test
    void delete() {
        var id = 66L;
        service.delete(id);
    }
}