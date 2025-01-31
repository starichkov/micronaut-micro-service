package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.service.TagService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/tags")
@ExecuteOn(TaskExecutors.IO)
public class TagsController {

    private final TagService service;

    @Inject
    public TagsController(TagService tagService) {
        this.service = tagService;
    }

    @Get("/{id}")
    public Tag get(Long id) {
        return service.get(id);
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Tag create(@Body Tag tag) {
        return service.create(tag);
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Tag update(@PathVariable("id") Long id, @Body Tag tag) {
        return service.update(id, tag);
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
