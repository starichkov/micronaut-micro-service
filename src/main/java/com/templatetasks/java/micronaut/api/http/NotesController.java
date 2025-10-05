package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.api.dto.NoteDto;
import com.templatetasks.java.micronaut.api.mapper.NoteDtoMapper;
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
    private final NoteDtoMapper mapper;

    @Inject
    public NotesController(NoteService noteService, NoteDtoMapper mapper) {
        this.service = noteService;
        this.mapper = mapper;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Page<NoteDto>> findAll(Pageable pageable) {
        return HttpResponse.ok(service.findAll(pageable).map(mapper::toDto));
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<NoteDto> get(@PathVariable("id") Long id) {
        return HttpResponse.ok(mapper.toDto(service.get(id)));
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<NoteDto> create(@Body NoteDto note) {
        return HttpResponse.ok(mapper.toDto(service.create(mapper.toDomain(note))));
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<NoteDto> update(@PathVariable("id") Long id, @Body NoteDto note) {
        return HttpResponse.ok(mapper.toDto(service.update(id, mapper.toDomain(note))));
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @Post(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<NoteDto> addTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(mapper.toDto(service.addTag(noteId, tagId)));
    }

    @Delete(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<NoteDto> removeTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(mapper.toDto(service.removeTag(noteId, tagId)));
    }
}
