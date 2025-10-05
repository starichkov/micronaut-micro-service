package com.templatetasks.java.micronaut.data.mapper;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class NoteMapperTest {

    @Inject
    NoteMapper mapper;

    @Test
    void mapNoteToEntity_withTags_andNull() {
        // null branch
        assertNull(mapper.map((Note) null));

        // with tags -> exercises tagSetToTagEntitySet
        var now = LocalDateTime.now();
        var t1 = new Tag();
        t1.setId(1L);
        t1.setLabel("t1");
        t1.setCreated(now);
        t1.setLastModified(now);
        var t2 = new Tag();
        t2.setId(2L);
        t2.setLabel("t2");
        Set<Tag> tags = new LinkedHashSet<>();
        tags.add(t1);
        tags.add(t2);

        var note = new Note();
        note.setId(100L);
        note.setTitle("title");
        note.setContent("content");
        note.setTags(tags);
        note.setCreated(now);
        note.setLastModified(now.plusMinutes(1));

        NoteEntity entity = mapper.map(note);
        assertNotNull(entity);
        assertEquals(note.getId(), entity.getId());
        assertEquals(note.getTitle(), entity.getTitle());
        assertEquals(note.getContent(), entity.getContent());
        assertNotNull(entity.getTags());
        assertEquals(2, entity.getTags().size());
        assertTrue(entity.getTags().stream().anyMatch(e -> "t1".equals(e.getLabel())));
        assertTrue(entity.getTags().stream().anyMatch(e -> "t2".equals(e.getLabel())));
        assertEquals(note.getCreated(), entity.getCreated());
        assertEquals(note.getLastModified(), entity.getLastModified());
    }

    @Test
    void mapEntityToNote_withTags_andNull() {
        // null branch
        assertNull(mapper.map((NoteEntity) null));

        // with tags -> exercises tagEntitySetToTagSet
        var now = LocalDateTime.now();
        var e1 = new TagEntity();
        e1.setId(11L);
        e1.setLabel("e1");
        e1.setCreated(now);
        var e2 = new TagEntity();
        e2.setId(12L);
        e2.setLabel("e2");
        Set<TagEntity> etags = new LinkedHashSet<>();
        etags.add(e1);
        etags.add(e2);

        var entity = new NoteEntity();
        entity.setId(200L);
        entity.setTitle("etitle");
        entity.setContent("econtent");
        entity.setTags(etags);
        entity.setCreated(now);
        entity.setLastModified(now.plusMinutes(2));

        Note note = mapper.map(entity);
        assertNotNull(note);
        assertEquals(entity.getId(), note.getId());
        assertEquals(entity.getTitle(), note.getTitle());
        assertEquals(entity.getContent(), note.getContent());
        assertNotNull(note.getTags());
        assertEquals(2, note.getTags().size());
        assertTrue(note.getTags().stream().anyMatch(t -> "e1".equals(t.getLabel())));
        assertTrue(note.getTags().stream().anyMatch(t -> "e2".equals(t.getLabel())));
        assertEquals(entity.getCreated(), note.getCreated());
        assertEquals(entity.getLastModified(), note.getLastModified());
    }

    @Test
    void mapNoteToEntity_nullAndEmptyTags() {
        // null tags: mapper should leave tags as null in entity
        var noteNullTags = new Note();
        noteNullTags.setId(300L);
        noteNullTags.setTitle("nt-null");
        noteNullTags.setContent("nc-null");
        // tags left null intentionally
        NoteEntity entityNull = mapper.map(noteNullTags);
        assertNotNull(entityNull);
        assertEquals(noteNullTags.getId(), entityNull.getId());
        assertNull(entityNull.getTags());

        // empty tags: should produce empty set (not null)
        var noteEmptyTags = new Note();
        noteEmptyTags.setId(301L);
        noteEmptyTags.setTitle("nt-empty");
        noteEmptyTags.setContent("nc-empty");
        noteEmptyTags.setTags(new java.util.LinkedHashSet<>());
        NoteEntity entityEmpty = mapper.map(noteEmptyTags);
        assertNotNull(entityEmpty);
        assertNotNull(entityEmpty.getTags());
        assertTrue(entityEmpty.getTags().isEmpty());
    }

    @Test
    void mapEntityToNote_nullAndEmptyTags() {
        // null tags on entity
        var entityNull = new NoteEntity();
        entityNull.setId(400L);
        entityNull.setTitle("et-null");
        entityNull.setContent("ec-null");
        // tags left null
        Note noteNull = mapper.map(entityNull);
        assertNotNull(noteNull);
        assertEquals(entityNull.getId(), noteNull.getId());
        assertNotNull(noteNull.getTags());
        assertTrue(noteNull.getTags().isEmpty());

        // empty tags on entity
        var entityEmpty = new NoteEntity();
        entityEmpty.setId(401L);
        entityEmpty.setTitle("et-empty");
        entityEmpty.setContent("ec-empty");
        entityEmpty.setTags(new java.util.LinkedHashSet<>());
        Note noteEmpty = mapper.map(entityEmpty);
        assertNotNull(noteEmpty);
        assertNotNull(noteEmpty.getTags());
        assertTrue(noteEmpty.getTags().isEmpty());
    }
}
