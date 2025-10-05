package com.templatetasks.java.micronaut.api.mapper;

import com.templatetasks.java.micronaut.api.dto.TagDto;
import com.templatetasks.java.micronaut.data.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TagDtoMapperTest {

    private final TagDtoMapper mapper = new TagDtoMapperImpl();

    @Test
    void toDto_mapsAllFields() {
        Tag tag = new Tag();
        tag.setId(42L);
        tag.setLabel("important");
        tag.setCreated(LocalDateTime.now().minusDays(1));
        tag.setLastModified(LocalDateTime.now());

        TagDto dto = mapper.toDto(tag);

        assertNotNull(dto);
        assertEquals(tag.getId(), dto.id());
        assertEquals(tag.getLabel(), dto.label());
        assertEquals(tag.getCreated(), dto.created());
        assertEquals(tag.getLastModified(), dto.lastModified());
    }

    @Test
    void toDomain_mapsAllFields() {
        TagDto dto = new TagDto(7L, "work", LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1));

        Tag tag = mapper.toDomain(dto);

        assertNotNull(tag);
        assertEquals(dto.id(), tag.getId());
        assertEquals(dto.label(), tag.getLabel());
        assertEquals(dto.created(), tag.getCreated());
        assertEquals(dto.lastModified(), tag.getLastModified());
    }

    @Test
    void nullSafety() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toDomain(null));
    }
}
