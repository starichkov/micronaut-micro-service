package com.templatetasks.java.micronaut.service.impl;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import com.templatetasks.java.micronaut.service.TagService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 12:29
 */
@MicronautTest
class TagServiceImplTest {

    @Inject
    EntityManager em;

    @Inject
    TagService service;

    @Test
    void getNotFound() {
        var id = 404L;
        assertNull(service.get(id));
    }

    @Test
    void get() {
        var entity = new TagEntity();
        entity.setLabel("tag-1");

        entity = em.merge(entity);
        em.getTransaction().commit();

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var result = service.get(id);
        assertNotNull(result);
        checkEquals(entity, result);
    }

    @Test
    void create() {
        var tag = new Tag();
        tag.setLabel("tag-2");

        var result = service.create(tag);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(tag.getLabel(), result.getLabel());
        assertNotNull(result.getCreated());
        assertNotNull(result.getLastModified());
    }

    @Test
    void updateNotFound() {
        var id = Long.MAX_VALUE; // ensure this ID does not exist in test DB

        var tag = new Tag();
        tag.setLabel("tag-3");

        var updated = service.update(id, tag);
        assertNull(updated);
    }

    @Test
    void update() {
        var entity = new TagEntity();
        entity.setLabel("tag-4");
        entity = em.merge(entity);
        em.getTransaction().commit();
        assertNotNull(entity);

        var id = entity.getId();
        assertNotNull(id);
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getLastModified());
        assertEquals(0, entity.getVersion());

        var tag = new Tag();
        tag.setLabel("tag-5");

        var result = service.update(id, tag);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(tag.getLabel(), result.getLabel());
    }

    @Test
    void delete() {
        var id = 66L;
        service.delete(id);
    }

    @Test
    void findAll() {
        // Create a few tags
        var tag1 = new TagEntity();
        tag1.setLabel("tag-1");

        var tag2 = new TagEntity();
        tag2.setLabel("tag-2");

        var tag3 = new TagEntity();
        tag3.setLabel("tag-3");

        tag1 = em.merge(tag1);
        tag2 = em.merge(tag2);
        tag3 = em.merge(tag3);
        em.getTransaction().commit();

        // Get all tags
        var tags = service.findAll();

        // Verify
        assertNotNull(tags);
        assertTrue(tags.size() >= 3);
        assertTrue(tags.stream().anyMatch(t -> t.getLabel().equals("tag-1")));
        assertTrue(tags.stream().anyMatch(t -> t.getLabel().equals("tag-2")));
        assertTrue(tags.stream().anyMatch(t -> t.getLabel().equals("tag-3")));
    }

    @Test
    void findAllPageable() {
        // Create some tags to paginate
        var e1 = new TagEntity(); e1.setLabel("p-tag-1");
        var e2 = new TagEntity(); e2.setLabel("p-tag-2");
        var e3 = new TagEntity(); e3.setLabel("p-tag-3");
        em.merge(e1); em.merge(e2); em.merge(e3);
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

    private void checkEquals(TagEntity entity, Tag tag) {
        assertEquals(entity.getId(), tag.getId());
        assertEquals(entity.getLabel(), tag.getLabel());
        assertEquals(entity.getCreated(), tag.getCreated());
        assertEquals(entity.getLastModified(), tag.getLastModified());
    }
}
