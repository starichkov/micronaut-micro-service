package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

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
}
