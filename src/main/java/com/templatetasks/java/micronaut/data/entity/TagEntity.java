package com.templatetasks.java.micronaut.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vadim Starichkov (starichkovva@gmail.com)
 * @since 14.09.2022 15:31
 */
@Entity
@Table(name = "tags")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"notes"})
public class TagEntity extends BaseEntity {

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime lastModified;

    @ManyToMany(mappedBy = "tags") // Many-to-many relationship mapped by "tags" field in Note
    private Set<NoteEntity> notes = new HashSet<>();

    @Version
    @Column(name = "version", nullable = false)
    private long version;
}
