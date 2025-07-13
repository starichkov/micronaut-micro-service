package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.service.NoteService;
import com.templatetasks.java.micronaut.service.impl.NoteServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 19.03.2023 12:21
 */
@MicronautTest
class NotesControllerTest {

    @Inject
    @Client("/notes")
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

    @MockBean(NoteServiceImpl.class)
    NoteService noteService() {
        return mock(NoteService.class);
    }
}