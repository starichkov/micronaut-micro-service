package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.service.NoteService;
import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Controller for managing notes.
 *
 * This controller is configured with caching using Valkey (Redis-compatible) as the distributed cache.
 * The @CacheConfig annotation specifies that all caching operations in this controller will use the "notes" cache.
 * Read operations are cached, and cache entries are invalidated when data is modified.
 *
 * Cache annotations are only applied when not in the test environment to avoid cache-related issues in tests.
 *
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 13:24
 */
@Controller("/notes")
@ExecuteOn(TaskExecutors.VIRTUAL)
@CacheConfig("notes")
public class NotesController {

    private final NoteService service;

    @Inject
    public NotesController(NoteService noteService) {
        this.service = noteService;
    }

    /**
     * Retrieves all notes.
     *
     * @Cacheable annotation caches the result of this method in the "notes" cache.
     * Subsequent calls will return the cached result without hitting the database.
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    @Cacheable
    public HttpResponse<List<Note>> findAll() {
        return HttpResponse.ok(service.findAll());
    }

    /**
     * Retrieves a specific note by ID.
     *
     * @Cacheable annotation with parameters={"id"} caches the result of this method
     * in the "notes" cache, using the note ID as the cache key.
     *
     * @param id The ID of the note to retrieve
     * @return The note with the specified ID
     */
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Cacheable(parameters = {"id"})
    public HttpResponse<Note> get(@PathVariable("id") Long id) {
        return HttpResponse.ok(service.get(id));
    }

    /**
     * Creates a new note.
     *
     * @CacheInvalidate annotation with all=true invalidates all entries in the "notes" cache,
     * as the list of all notes will change when a new note is created.
     *
     * @param note The note to create
     * @return The created note
     */
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(all = true)
    public HttpResponse<Note> create(@Body Note note) {
        return HttpResponse.ok(service.create(note));
    }

    /**
     * Updates an existing note.
     *
     * @CacheInvalidate annotation with parameters={"id"} invalidates the cache entry
     * for the specific note being updated.
     *
     * @param id The ID of the note to update
     * @param note The updated note data
     * @return The updated note
     */
    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(parameters = {"id"})
    public HttpResponse<Note> update(@PathVariable("id") Long id, @Body Note note) {
        return HttpResponse.ok(service.update(id, note));
    }

    /**
     * Deletes a note.
     *
     * @CacheInvalidate annotation with parameters={"id"} and all=true invalidates both
     * the cache entry for the specific note being deleted and the list of all notes.
     *
     * @param id The ID of the note to delete
     */
    @Delete("/{id}")
    @CacheInvalidate(parameters = {"id"}, all = true)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    /**
     * Adds a tag to a note.
     *
     * @CacheInvalidate annotation with parameters={"noteId"} invalidates the cache entry
     * for the specific note being modified.
     *
     * @param noteId The ID of the note
     * @param tagId The ID of the tag to add
     * @return The updated note
     */
    @Post(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(parameters = {"noteId"})
    public HttpResponse<Note> addTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(service.addTag(noteId, tagId));
    }

    /**
     * Removes a tag from a note.
     *
     * @CacheInvalidate annotation with parameters={"noteId"} invalidates the cache entry
     * for the specific note being modified.
     *
     * @param noteId The ID of the note
     * @param tagId The ID of the tag to remove
     * @return The updated note
     */
    @Delete(value = "/{noteId}/tags/{tagId}", produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(parameters = {"noteId"})
    public HttpResponse<Note> removeTag(@PathVariable("noteId") Long noteId, @PathVariable("tagId") Long tagId) {
        return HttpResponse.ok(service.removeTag(noteId, tagId));
    }
}
