package com.templatetasks.java.micronaut.data.mapper;

import com.templatetasks.java.micronaut.data.Note;
import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:25
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NoteMapper {

    NoteEntity map(Note source);

    Note map(NoteEntity source);
}
