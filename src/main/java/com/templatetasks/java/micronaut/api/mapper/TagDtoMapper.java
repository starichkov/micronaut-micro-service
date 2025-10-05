package com.templatetasks.java.micronaut.api.mapper;

import com.templatetasks.java.micronaut.api.dto.TagDto;
import com.templatetasks.java.micronaut.data.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagDtoMapper {

    TagDto toDto(Tag source);

    Tag toDomain(TagDto source);
}
