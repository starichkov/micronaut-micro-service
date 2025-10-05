package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.service.NoteService;
import com.templatetasks.java.micronaut.service.exception.NotFoundException;
import com.templatetasks.java.micronaut.service.impl.NoteServiceImpl;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 12:21
 */
@MicronautTest
class NotesControllerTest {

    @Inject
    @Client("/v1/notes")
    HttpClient client;

    @Inject
    NoteService noteService;

    @Test
    void get() {
        var id = 1L;

        var note = new Note();
        note.setId(id);
        note.setTitle("Junk");
        note.setContent("Real nice piece of junk!");

        when(noteService.get(eq(id)))
                .thenReturn(note);

        Note response = client.toBlocking()
                                   .retrieve(HttpRequest.GET("/" + id), Note.class);

        assertNotNull(response);
        assertEquals(note.getId(), response.getId());
        assertEquals(note.getTitle(), response.getTitle());
        assertEquals(note.getContent(), response.getContent());
    }

    @Test
    void create() {
        var id = 2L;

        var note = new Note();
        note.setTitle("Awesome Junk");
        note.setContent("Really awesome and shiny piece of junk!");

        doAnswer(invocationOnMock -> {
            Note saved = invocationOnMock.getArgument(0);
            saved.setId(id);
            return saved;
        }).when(noteService).create(eq(note));

        Note response = client.toBlocking()
                                   .retrieve(HttpRequest.POST("/", note), Note.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(note.getTitle(), response.getTitle());
        assertEquals(note.getContent(), response.getContent());
    }

    @Test
    void update() {
        var id = 3L;

        var note = new Note();
        note.setTitle("Superb Junk");
        note.setContent("You'll never find better junk that this!");

        doAnswer(invocationOnMock -> {
            Long paramId = invocationOnMock.getArgument(0);
            Note paramNote = invocationOnMock.getArgument(1);
            paramNote.setId(paramId);
            return paramNote;
        }).when(noteService).update(eq(id), eq(note));

        Note response = client.toBlocking()
                                   .retrieve(HttpRequest.PATCH("/" + id, note), Note.class);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(note.getTitle(), response.getTitle());
        assertEquals(note.getContent(), response.getContent());
    }

    @Test
    void delete() {
        var id = 4L;

        HttpResponse<Void> response = client.toBlocking()
                                              .exchange(HttpRequest.DELETE("/" + id));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(200, response.code());
    }

    @Test
    void listPaginated() {
        // Prepare mock page
        var note1 = new Note();
        note1.setId(10L);
        note1.setTitle("N1");
        note1.setContent("C1");
        var note2 = new Note();
        note2.setId(11L);
        note2.setTitle("N2");
        note2.setContent("C2");
        var pageable = Pageable.from(0, 2);
        var page = Page.of(List.of(note1, note2), pageable, 5L);

        when(noteService.findAll(any(Pageable.class))).thenReturn(page);

        var request = HttpRequest.GET("/?page=0&size=2");
        Page<Note> response = client.toBlocking()
                .retrieve(request, Argument.of(Page.class, Note.class));

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(5, response.getTotalSize());
        assertEquals(3, response.getTotalPages());
        assertEquals(0, response.getPageNumber());
        assertEquals(2, response.getSize());
        assertEquals("N1", response.getContent().get(0).getTitle());
        assertEquals("N2", response.getContent().get(1).getTitle());
    }

    @Test
    void addTag() {
        var noteId = 5L;
        var tagId = 100L;

        var tag = new Tag();
        tag.setId(tagId);
        tag.setLabel("important");

        var note = new Note();
        note.setId(noteId);
        note.setTitle("Tagged Note");
        note.setContent("This note has a tag!");
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        note.setTags(tags);

        when(noteService.addTag(eq(noteId), eq(tagId)))
                .thenReturn(note);

        Note response = client.toBlocking()
                .retrieve(HttpRequest.POST("/" + noteId + "/tags/" + tagId, ""), Note.class);

        assertNotNull(response);
        assertEquals(noteId, response.getId());
        assertEquals(note.getTitle(), response.getTitle());
        assertEquals(note.getContent(), response.getContent());
        assertNotNull(response.getTags());
        assertEquals(1, response.getTags().size());
        assertTrue(response.getTags().stream().anyMatch(t -> t.getId().equals(tagId)));
    }

    @Test
    void addTagWhenNotFoundExceptionThrown() {
        var noteId = 7L;
        var tagId = 300L;

        when(noteService.addTag(eq(noteId), eq(tagId)))
                .thenThrow(new NotFoundException("Note or tag not found"));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(HttpRequest.POST("/" + noteId + "/tags/" + tagId, ""), Note.class)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    void removeTag() {
        var noteId = 6L;
        var tagId = 200L;

        var note = new Note();
        note.setId(noteId);
        note.setTitle("Untagged Note");
        note.setContent("This note had a tag removed!");
        note.setTags(new HashSet<>());

        when(noteService.removeTag(eq(noteId), eq(tagId)))
                .thenReturn(note);

        Note response = client.toBlocking()
                .retrieve(HttpRequest.DELETE("/" + noteId + "/tags/" + tagId), Note.class);

        assertNotNull(response);
        assertEquals(noteId, response.getId());
        assertEquals(note.getTitle(), response.getTitle());
        assertEquals(note.getContent(), response.getContent());
        assertTrue(response.getTags() == null || response.getTags().isEmpty());
    }

    @Test
    void removeTagWhenNotFoundExceptionThrown() {
        var noteId = 8L;
        var tagId = 400L;

        when(noteService.removeTag(eq(noteId), eq(tagId)))
                .thenThrow(new NotFoundException("Note or tag not found"));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().retrieve(HttpRequest.DELETE("/" + noteId + "/tags/" + tagId), Note.class)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @MockBean(NoteServiceImpl.class)
    NoteService noteService() {
        return mock(NoteService.class);
    }
}