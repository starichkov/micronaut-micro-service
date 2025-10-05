package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.service.TagService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/v1/tags")
@ExecuteOn(TaskExecutors.VIRTUAL)
public class TagsController {

    private final TagService service;

    @Inject
    public TagsController(TagService tagService) {
        this.service = tagService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<Tag>> findAll() {
        return HttpResponse.ok(service.findAll());
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Tag> get(Long id) {
        return HttpResponse.ok(service.get(id));
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Tag> create(@Body Tag tag) {
        return HttpResponse.ok(service.create(tag));
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Tag> update(@PathVariable("id") Long id, @Body Tag tag) {
        return HttpResponse.ok(service.update(id, tag));
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
