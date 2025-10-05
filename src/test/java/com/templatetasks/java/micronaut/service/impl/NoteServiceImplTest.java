package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
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
        var id = Long.MAX_VALUE; // ensure this ID does not exist in test DB

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

    @Test
    void findAll() {
        // Create a few notes
        var note1 = new NoteEntity();
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        var note2 = new NoteEntity();
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        var note3 = new NoteEntity();
        note3.setTitle("Note 3");
        note3.setContent("Content 3");

        note1 = em.merge(note1);
        note2 = em.merge(note2);
        note3 = em.merge(note3);
        em.getTransaction().commit();

        // Get all notes
        var notes = service.findAll();

        // Verify
        assertNotNull(notes);
        assertTrue(notes.size() >= 3);
        assertTrue(notes.stream().anyMatch(n -> n.getTitle().equals("Note 1")));
        assertTrue(notes.stream().anyMatch(n -> n.getTitle().equals("Note 2")));
        assertTrue(notes.stream().anyMatch(n -> n.getTitle().equals("Note 3")));
    }

    @Test
    void findAllPageable() {
        // Create some notes to paginate
        var n1 = new NoteEntity(); n1.setTitle("P1"); n1.setContent("PC1");
        var n2 = new NoteEntity(); n2.setTitle("P2"); n2.setContent("PC2");
        var n3 = new NoteEntity(); n3.setTitle("P3"); n3.setContent("PC3");
        em.merge(n1); em.merge(n2); em.merge(n3);
        em.getTransaction().commit();

        var page0 = service.findAll(io.micronaut.data.model.Pageable.from(0, 2));
        assertNotNull(page0);
        assertEquals(2, page0.getContent().size());
        assertTrue(page0.getTotalSize() >= 3);

        var page1 = service.findAll(io.micronaut.data.model.Pageable.from(1, 2));
        assertNotNull(page1);
        assertTrue(page1.getContent().size() >= 1);
        assertEquals(page0.getTotalSize(), page1.getTotalSize());
    }

    @Test
    void addTagNotFound() {
        var noteId = 404L;
        var tagId = 1L;

        var result = service.addTag(noteId, tagId);
        assertNull(result);
    }

    @Test
    void addTag() {
        // Create a note and a tag
        var note = new NoteEntity();
        note.setTitle("Note with tag");
        note.setContent("This note will have a tag");

        var tag = new TagEntity();
        tag.setLabel("test-tag");

        note = em.merge(note);
        tag = em.merge(tag);
        em.getTransaction().commit();

        // Add the tag to the note
        var result = service.addTag(note.getId(), tag.getId());

        // Verify
        assertNotNull(result);
        assertEquals(note.getId(), result.getId());
        assertEquals(note.getTitle(), result.getTitle());
        assertEquals(note.getContent(), result.getContent());

        // Verify the tag was added by getting the note again
        var updatedNote = service.get(note.getId());
        assertNotNull(updatedNote);
        assertNotNull(updatedNote.getTags());
        assertFalse(updatedNote.getTags().isEmpty());
        assertEquals(1, updatedNote.getTags().size());
        assertEquals(tag.getLabel(), updatedNote.getTags().iterator().next().getLabel());
    }

    @Test
    void removeTagNotFound() {
        var noteId = 404L;
        var tagId = 1L;

        var result = service.removeTag(noteId, tagId);
        assertNull(result);
    }

    @Test
    void removeTag() {
        // Create a note and a tag
        var note = new NoteEntity();
        note.setTitle("Note with tag to remove");
        note.setContent("This note will have a tag that gets removed");

        var tag = new TagEntity();
        tag.setLabel("tag-to-remove");

        note = em.merge(note);
        tag = em.merge(tag);

        // Add the tag to the note
        note.addTag(tag);
        note = em.merge(note);
        em.getTransaction().commit();

        // Remove the tag from the note
        var result = service.removeTag(note.getId(), tag.getId());

        // Verify
        assertNotNull(result);
        assertEquals(note.getId(), result.getId());
        assertEquals(note.getTitle(), result.getTitle());
        assertEquals(note.getContent(), result.getContent());

        // Verify the tag was removed by getting the note again
        var updatedNote = service.get(note.getId());
        assertNotNull(updatedNote);
        assertNotNull(updatedNote.getTags());
        assertTrue(updatedNote.getTags().isEmpty());
    }
}
