package com.templatetasks.java.micronaut.data.mapper;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TagMapperTest {

    @Inject
    TagMapper mapper;

    @Test
    void mapTagToEntity_andNull() {
        // null branch
        assertNull(mapper.map((Tag) null));

        // value branch
        var now = LocalDateTime.now();
        var tag = new Tag();
        tag.setId(42L);
        tag.setLabel("alpha");
        tag.setCreated(now);
        tag.setLastModified(now.plusSeconds(1));

        TagEntity entity = mapper.map(tag);
        assertNotNull(entity);
        assertEquals(tag.getId(), entity.getId());
        assertEquals(tag.getLabel(), entity.getLabel());
        assertEquals(tag.getCreated(), entity.getCreated());
        assertEquals(tag.getLastModified(), entity.getLastModified());
    }

    @Test
    void mapEntityToTag_andNull() {
        // null branch
        assertNull(mapper.map((TagEntity) null));

        // value branch
        var now = LocalDateTime.now();
        var entity = new TagEntity();
        entity.setId(7L);
        entity.setLabel("beta");
        entity.setCreated(now);
        entity.setLastModified(now.plusSeconds(2));

        Tag tag = mapper.map(entity);
        assertNotNull(tag);
        assertEquals(entity.getId(), tag.getId());
        assertEquals(entity.getLabel(), tag.getLabel());
        assertEquals(entity.getCreated(), tag.getCreated());
        assertEquals(entity.getLastModified(), tag.getLastModified());
    }
}
