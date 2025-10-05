package com.templatetasks.java.micronaut.api.mapper;

import com.templatetasks.java.micronaut.api.dto.NoteDto;
import com.templatetasks.java.micronaut.data.Note;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = { TagDtoMapper.class })
public interface NoteDtoMapper {

    NoteDto toDto(Note source);

    Note toDomain(NoteDto source);
}
