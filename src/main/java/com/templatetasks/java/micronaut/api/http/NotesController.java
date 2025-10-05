package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:24
 */
@Controller("/v1/notes")
@ExecuteOn(TaskExecutors.VIRTUAL)
public class NotesController {

    private final NoteService service;

    @Inject
    public NotesController(NoteService noteService) {
        this.service = noteService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Page<Note>> findAll(Pageable pageable) {
        return HttpResponse.ok(service.findAll(pageable));
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Note> get(@PathVariable("id") Long id) {
        return HttpResponse.ok(service.get(id));
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Note> create(@Body Note note) {
        return HttpResponse.ok(service.create(note));
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Note> update(@PathVariable("id") Long id, @Body Note note) {
        return HttpResponse.ok(service.update(id, note));
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @Post(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Note> addTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(service.addTag(noteId, tagId));
    }

    @Delete(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Note> removeTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(service.removeTag(noteId, tagId));
    }
}
