package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.service.TagService;
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
 * Controller for managing tags.
 *
 * This controller is configured with caching using Valkey (Redis-compatible) as the distributed cache.
 * The @CacheConfig annotation specifies that all caching operations in this controller will use the "tags" cache.
 * Read operations are cached, and cache entries are invalidated when data is modified.
 *
 * Cache annotations are only applied when not in the test environment to avoid cache-related issues in tests.
 *
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/tags")
@ExecuteOn(TaskExecutors.VIRTUAL)
@CacheConfig("tags")
public class TagsController {

    private final TagService service;

    @Inject
    public TagsController(TagService tagService) {
        this.service = tagService;
    }

    /**
     * Retrieves all tags.
     *
     * @Cacheable annotation caches the result of this method in the "tags" cache.
     * Subsequent calls will return the cached result without hitting the database.
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    @Cacheable
    public HttpResponse<List<Tag>> findAll() {
        return HttpResponse.ok(service.findAll());
    }

    /**
     * Retrieves a specific tag by ID.
     *
     * @Cacheable annotation with parameters={"id"} caches the result of this method
     * in the "tags" cache, using the tag ID as the cache key.
     *
     * @param id The ID of the tag to retrieve
     * @return The tag with the specified ID
     */
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Cacheable(parameters = {"id"})
    public HttpResponse<Tag> get(Long id) {
        return HttpResponse.ok(service.get(id));
    }

    /**
     * Creates a new tag.
     *
     * @CacheInvalidate annotation with all=true invalidates all entries in the "tags" cache,
     * as the list of all tags will change when a new tag is created.
     *
     * @param tag The tag to create
     * @return The created tag
     */
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(all = true)
    public HttpResponse<Tag> create(@Body Tag tag) {
        return HttpResponse.ok(service.create(tag));
    }

    /**
     * Updates an existing tag.
     *
     * @CacheInvalidate annotation with parameters={"id"} invalidates the cache entry
     * for the specific tag being updated.
     *
     * @param id The ID of the tag to update
     * @param tag The updated tag data
     * @return The updated tag
     */
    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @CacheInvalidate(parameters = {"id"})
    public HttpResponse<Tag> update(@PathVariable("id") Long id, @Body Tag tag) {
        return HttpResponse.ok(service.update(id, tag));
    }

    /**
     * Deletes a tag.
     *
     * @CacheInvalidate annotation with parameters={"id"} and all=true invalidates both
     * the cache entry for the specific tag being deleted and the list of all tags.
     *
     * @param id The ID of the tag to delete
     */
    @Delete("/{id}")
    @CacheInvalidate(parameters = {"id"}, all = true)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
