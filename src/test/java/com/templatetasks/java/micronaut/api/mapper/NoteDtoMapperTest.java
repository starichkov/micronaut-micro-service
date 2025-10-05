package com.templatetasks.java.micronaut.api.mapper;

import com.templatetasks.java.micronaut.api.dto.NoteDto;
import com.templatetasks.java.micronaut.api.dto.TagDto;
import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteDtoMapperTest {

    private final NoteDtoMapper mapper = new NoteDtoMapperImpl(new TagDtoMapperImpl());

    @Test
    void toDto_mapsAllFieldsWithNestedTags() {
        Tag tag = new Tag();
        tag.setId(3L);
        tag.setLabel("personal");
        tag.setCreated(LocalDateTime.now().minusDays(3));
        tag.setLastModified(LocalDateTime.now().minusDays(2));

        Note note = new Note();
        note.setId(1L);
        note.setTitle("Title");
        note.setContent("Content");
        note.setCreated(LocalDateTime.now().minusDays(5));
        note.setLastModified(LocalDateTime.now().minusDays(1));
        note.setTags(new HashSet<>(Set.of(tag)));

        NoteDto dto = mapper.toDto(note);

        assertNotNull(dto);
        assertEquals(note.getId(), dto.id());
        assertEquals(note.getTitle(), dto.title());
        assertEquals(note.getContent(), dto.content());
        assertEquals(note.getCreated(), dto.created());
        assertEquals(note.getLastModified(), dto.lastModified());
        assertNotNull(dto.tags());
        assertEquals(1, dto.tags().size());
        TagDto mappedTag = dto.tags().iterator().next();
        assertEquals(tag.getId(), mappedTag.id());
        assertEquals(tag.getLabel(), mappedTag.label());
        assertEquals(tag.getCreated(), mappedTag.created());
        assertEquals(tag.getLastModified(), mappedTag.lastModified());
    }

    @Test
    void toDomain_mapsAllFieldsWithNestedTags() {
        TagDto tagDto = new TagDto(11L, "urgent", LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(9));

        NoteDto dto = new NoteDto(5L,
                "N title",
                "N content",
                new HashSet<>(Set.of(tagDto)),
                LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(15));

        Note note = mapper.toDomain(dto);

        assertNotNull(note);
        assertEquals(dto.id(), note.getId());
        assertEquals(dto.title(), note.getTitle());
        assertEquals(dto.content(), note.getContent());
        assertEquals(dto.created(), note.getCreated());
        assertEquals(dto.lastModified(), note.getLastModified());
        assertNotNull(note.getTags());
        assertEquals(1, note.getTags().size());
        Tag mappedTag = note.getTags().iterator().next();
        assertEquals(tagDto.id(), mappedTag.getId());
        assertEquals(tagDto.label(), mappedTag.getLabel());
        assertEquals(tagDto.created(), mappedTag.getCreated());
        assertEquals(tagDto.lastModified(), mappedTag.getLastModified());
    }

    @Test
    void nullSafety() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toDomain(null));
    }
}
