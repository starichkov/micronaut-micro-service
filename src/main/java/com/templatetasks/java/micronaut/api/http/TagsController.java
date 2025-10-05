package com.templatetasks.java.micronaut.api.http;

import com.templatetasks.java.micronaut.api.dto.TagDto;
import com.templatetasks.java.micronaut.api.mapper.TagDtoMapper;
import com.templatetasks.java.micronaut.service.TagService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:24
 */
@Controller("/v1/tags")
@ExecuteOn(TaskExecutors.VIRTUAL)
public class TagsController {

    private final TagService service;
    private final TagDtoMapper mapper;

    @Inject
    public TagsController(TagService tagService, TagDtoMapper mapper) {
        this.service = tagService;
        this.mapper = mapper;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<TagDto>> findAll() {
        return HttpResponse.ok(service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<TagDto> get(Long id) {
        return HttpResponse.ok(mapper.toDto(service.get(id)));
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<TagDto> create(@Body TagDto tag) {
        return HttpResponse.ok(mapper.toDto(service.create(mapper.toDomain(tag))));
    }

    @Patch(value = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<TagDto> update(@PathVariable("id") Long id, @Body TagDto tag) {
        return HttpResponse.ok(mapper.toDto(service.update(id, mapper.toDomain(tag))));
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
