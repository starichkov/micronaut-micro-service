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
 * @since 14.09.2022 16:20
 */
@Entity
@Table(name = "notes")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"tags"})
public class NoteEntity extends BaseEntity {

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = true)
    private String content;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "note_tag", // Name of the join table
            joinColumns = @JoinColumn(name = "note_id"), // Foreign key in the join table for Note
            inverseJoinColumns = @JoinColumn(name = "tag_id") // Foreign key in the join table for Tag
    )
    private Set<TagEntity> tags = new HashSet<>(); // Use Set to avoid duplicates

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime lastModified;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    // Helper method to add a tag
    public void addTag(TagEntity tag) {
        this.tags.add(tag);
        tag.getNotes().add(this);
    }

    // Helper method to remove a tag
    public void removeTag(TagEntity tag) {
        this.tags.remove(tag);
        tag.getNotes().remove(this);
    }
}
