package com.templatetasks.java.micronaut.data.mapper;

import com.templatetasks.java.micronaut.data.Tag;
import com.templatetasks.java.micronaut.data.entity.TagEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 17.03.2023 21:33
 */
@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TagMapper {

    TagEntity map(Tag source);

    Tag map(TagEntity source);
}
