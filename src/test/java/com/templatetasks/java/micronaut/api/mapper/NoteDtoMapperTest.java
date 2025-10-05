package com.templatetasks.java.micronaut.api.mapper;

import com.templatetasks.java.micronaut.api.dto.NoteDto;
import com.templatetasks.java.micronaut.api.dto.TagDto;
import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.Tag;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class NoteDtoMapperTest {

    @Inject
    NoteDtoMapper mapper;

    @Test
    void mapNoteToDto_withTags_andNullAndEmpty() {
        // null source
        assertNull(mapper.toDto(null));

        // with non-empty tags -> exercises tagSetToTagDtoSet
        var now = LocalDateTime.now();
        var t1 = new Tag();
        t1.setId(1L);
        t1.setLabel("t1");
        t1.setCreated(now);
        t1.setLastModified(now.plusSeconds(1));
        var t2 = new Tag();
        t2.setId(2L);
        t2.setLabel("t2");
        t2.setCreated(now);
        t2.setLastModified(now.plusSeconds(2));
        Set<Tag> tags = new LinkedHashSet<>();
        tags.add(t1); tags.add(t2);

        var note = new Note();
        note.setId(100L);
        note.setTitle("n");
        note.setContent("c");
        note.setTags(tags);
        note.setCreated(now);
        note.setLastModified(now.plusMinutes(1));

        NoteDto dto = mapper.toDto(note);
        assertNotNull(dto);
        assertEquals(note.getId(), dto.id());
        assertEquals(note.getTitle(), dto.title());
        assertEquals(note.getContent(), dto.content());
        assertNotNull(dto.tags());
        assertEquals(2, dto.tags().size());
        assertTrue(dto.tags().stream().anyMatch(d -> "t1".equals(d.label())));
        assertTrue(dto.tags().stream().anyMatch(d -> "t2".equals(d.label())));
        assertEquals(note.getCreated(), dto.created());
        assertEquals(note.getLastModified(), dto.lastModified());

        // empty tags -> should produce empty set (not null)
        var noteEmpty = new Note();
        noteEmpty.setId(101L);
        noteEmpty.setTitle("ne");
        noteEmpty.setContent("ce");
        noteEmpty.setTags(new LinkedHashSet<>());
        NoteDto dtoEmpty = mapper.toDto(noteEmpty);
        assertNotNull(dtoEmpty);
        assertNotNull(dtoEmpty.tags());
        assertTrue(dtoEmpty.tags().isEmpty());

        // null tags -> should remain null in DTO tags field
        var noteNullTags = new Note();
        noteNullTags.setId(102L);
        noteNullTags.setTitle("nn");
        noteNullTags.setContent("cn");
        NoteDto dtoNull = mapper.toDto(noteNullTags);
        assertNotNull(dtoNull);
        assertNull(dtoNull.tags());
    }

    @Test
    void mapDtoToNote_withTags_andNullAndEmpty() {
        // null source
        assertNull(mapper.toDomain(null));

        // with non-empty tags -> exercises tagDtoSetToTagSet
        var now = LocalDateTime.now();
        var d1 = new TagDto(11L, "d1", now, now);
        var d2 = new TagDto(12L, "d2", now, now.plusSeconds(1));
        Set<TagDto> dtos = new LinkedHashSet<>();
        dtos.add(d1); dtos.add(d2);

        var dto = new NoteDto(200L, "t", "c", dtos, now, now.plusMinutes(2));
        Note note = mapper.toDomain(dto);
        assertNotNull(note);
        assertEquals(dto.id(), note.getId());
        assertEquals(dto.title(), note.getTitle());
        assertEquals(dto.content(), note.getContent());
        assertNotNull(note.getTags());
        assertEquals(2, note.getTags().size());
        assertTrue(note.getTags().stream().anyMatch(t -> "d1".equals(t.getLabel())));
        assertTrue(note.getTags().stream().anyMatch(t -> "d2".equals(t.getLabel())));
        assertEquals(dto.created(), note.getCreated());
        assertEquals(dto.lastModified(), note.getLastModified());

        // empty tags -> should create empty set on domain
        var dtoEmpty = new NoteDto(201L, "te", "ce", new LinkedHashSet<>(), now, now.plusSeconds(5));
        Note noteEmpty = mapper.toDomain(dtoEmpty);
        assertNotNull(noteEmpty);
        assertNotNull(noteEmpty.getTags());
        assertTrue(noteEmpty.getTags().isEmpty());

        // null tags -> should create empty set (Note domain typically keeps empty set via NoteEntity mapping,
        // but for DTO->domain we accept null to mean no tags -> keep null or empty?
        // Generated mapper for set returns null when input is null; verify behavior to cover the null branch.
        var dtoNull = new NoteDto(202L, "tn", "cn", null, now, now);
        Note noteNull = mapper.toDomain(dtoNull);
        assertNotNull(noteNull);
        assertNull(noteNull.getTags());
    }
}
