package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.service.TagService;
import com.templatetasks.java.micronaut.service.impl.TagServiceImpl;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 18.03.2023 22:36
 */
@MicronautTest
class TagsControllerTest {

    @Inject
    @Client("/tags")
    HttpClient client;

    @Inject
    TagService tagService;

    @Test
    void get() {
        var id = 1L;

        var tag = new Tag();
        tag.setId(id);
        tag.setLabel("tag-1");

        when(tagService.get(eq(id)))
                .thenReturn(tag);

        Tag response = client.toBlocking()
                               .retrieve(HttpRequest.GET("/" + id), Tag.class);

        assertNotNull(response);
        assertEquals(tag.getId(), response.getId());
        assertEquals(tag.getLabel(), response.getLabel());
    }

    @Test
    void create() {
        var id = 2L;

        var tag = new Tag();
        tag.setLabel("tag-2");

        doAnswer(invocationOnMock -> {
            Tag saved = invocationOnMock.getArgument(0);
            saved.setId(id);
            return saved;
        }).when(tagService).create(eq(tag));

        Tag response = client.toBlocking()
                               .retrieve(HttpRequest.POST("/", tag), Tag.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(tag.getLabel(), response.getLabel());
    }

    @Test
    void update() {
        var id = 3L;

        var tag = new Tag();
        tag.setLabel("tag-3");

        doAnswer(invocationOnMock -> {
            Long paramId = invocationOnMock.getArgument(0);
            Tag paramTag = invocationOnMock.getArgument(1);
            paramTag.setId(paramId);
            return paramTag;
        }).when(tagService).update(eq(id), eq(tag));

        Tag response = client.toBlocking()
                               .retrieve(HttpRequest.PATCH("/" + id, tag), Tag.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(tag.getLabel(), response.getLabel());
    }

    @Test
    void delete() {
        var id = 4L;

        HttpResponse<Void> response = client.toBlocking()
                                              .exchange(HttpRequest.DELETE("/" + id));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.code());
    }

    @MockBean(TagServiceImpl.class)
    TagService tagService() {
        return mock(TagService.class);
    }
}