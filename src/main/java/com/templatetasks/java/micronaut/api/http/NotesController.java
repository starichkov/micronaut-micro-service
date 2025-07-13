package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:24
 */
@Controller("/notes")
@ExecuteOn(TaskExecutors.IO)
public class NotesController {

    private final NoteService service;

    @Inject
    public NotesController(NoteService noteService) {
        this.service = noteService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<Note> findAll() {
        return service.findAll();
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public Note get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Note create(@Body Note note) {
        return service.create(note);
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Note update(@PathVariable("id") Long id, @Body Note note) {
        return service.update(id, note);
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @Post(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public Note addTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return service.addTag(noteId, tagId);
    }

    @Delete(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public Note removeTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return service.removeTag(noteId, tagId);
    }
}
