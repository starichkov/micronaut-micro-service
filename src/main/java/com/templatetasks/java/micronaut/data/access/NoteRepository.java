package com.templatetasks.java.micronaut.data.access;

import com.templatetasks.java.micronaut.data.entity.NoteEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 16:43
 */
@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
}
